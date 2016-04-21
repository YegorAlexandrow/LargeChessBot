
#define CMD_HOME    B00000001 //Команда "Встать в положение ожидания хода"
#define CMD_CLOCK   B00000010 //Команда "Нажать на часы"
#define CMD_FINISH  B00111111 //Команда "Закончить работу"
#define HEADER_OUT  B11000000 //Заголовок команды "Убрать фигуру с доски"
#define HEADER_PUT  B01000000 //Заголовок команды "Поставить фигуру"
#define HEADER_TAKE B10000000 //Заголовок команды "Взять фигуру"

#define FIRST_PIN 2  //Пины для комадны: со 2 по 9
#define RX_PIN    10 //Пин для принятия сигнала с манипулятора
#define CLOCK_PIN 11 //Пин для кнопки с часов

//Сигналы с компьютера
#define MOVE_MARK 'M'        //Передвинуть фигуру
#define NEW_GAME_MARK 'N'    //Начать новую игру
#define CAPTURE_FIG_MARK 'C' //Убрать фигуру с доски
#define FINISH_MARK 'F' //Убрать фигуру с доски



byte array_list[16],   //Массив, хранящий очередь команд манипулятору
     ptr = 0,          //Следующая команда для отправки
     ptr_last = 0,     //Последняя записанная команда
     clock_signal = 0; //Текущий сигнал с часов


//Добавление команды в массив
void addToArrayList(byte n) {
  array_list[(ptr_last++) % 16] = n;
}

//Отправка команды роботу
void sendNextCommand() {
  for (int i = 0; i < 8; ++i) {
    digitalWrite(FIRST_PIN + i, !((array_list[ptr % 16] >> i) % 2));
  }
  
  if(array_list[ptr % 16] == CMD_HOME) delay(5500);
  if(array_list[ptr % 16] >> 6 == 3) delay(5500);
  
  ptr++;
}

//Совершение хода
void makeMove() {
  addToArrayList(HEADER_TAKE + Serial.read());
  addToArrayList(HEADER_PUT  + Serial.read());
}

bool pval = 0;
void setup() {
  Serial.begin(9600);

  for (int i = 0; i < 8; ++i) {
    pinMode(FIRST_PIN+i, OUTPUT);
    digitalWrite(FIRST_PIN+i, 1);
  }
  pinMode(RX_PIN, INPUT);
  pinMode(CLOCK_PIN, INPUT);
}

void loop() {
  
  //Чтение с последовательного порта
  if (Serial.available() > 0) {
    delay(20);

    byte mark = Serial.read();

    switch (mark) {
      case CAPTURE_FIG_MARK: 
          addToArrayList(HEADER_OUT + Serial.read()); break;
      
      case MOVE_MARK: 
          makeMove(); addToArrayList(CMD_HOME); break;
          
      case 'm': 
          makeMove(); break;
      
      case NEW_GAME_MARK: 
          addToArrayList(CMD_HOME); break;
     
      case FINISH_MARK: 
          addToArrayList(CMD_FINISH); break;
      
      default: Serial.flush(); break;
    }
  }
  //Если часы были переключены, мы сообщаем об этом компьютеру
  if(clock_signal != digitalRead(CLOCK_PIN)) {
    if(clock_signal = digitalRead(CLOCK_PIN)) {
      Serial.write(1);
      delay(120);
    }
  }
  
  //Если робот готов принять следующую команду (а также есть, что отправлять),
  //Отправляем её и ждём, когда робот на нее среагирует
    if(digitalRead(RX_PIN) && ptr != ptr_last) {
        sendNextCommand();
        pval = false;
        delay(7500);
    }
      
  delay(35);
}
