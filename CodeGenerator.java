import java.util.Scanner;

//Этот класс отвечает за непосредственную генерацию кода
public class CodeGenerator {
	private String out; //Вся программа для робота записывается сюда
	
	public CodeGenerator(String src) {
	}
	
	//Эта функция преобразует логические значения
	private String decodeBool(String s) {
		return "0".equals(s) ? "FALSE" : "TRUE";
	}
	
	//Ставит требуемео количество табов(нужно исключительно для красоты)
	private String tab(int n) {
		String s = "";
		
		for(int i=0; i<n; ++i) s += "\t";
		
		return s;
	}
	
	//Эта функция обрабатывает все известные ей команды, 
	//подробнее о каждой написаено в info.txt
	private String checkCommand(String cmd) {
		if("SENDSIG".equals(cmd.toUpperCase())) {
			return tab(t) + "out[0] = " + decodeBool(in.next()) + ";\n";
		}
		
		if("LOOP".equals(cmd.toUpperCase())) {
			loop = true;
			return tab(t++) + "while(TRUE) {\n";
		}
		
		if("CASECMD".equals(cmd.toUpperCase())) {
			return tab(t++) + "if(in[7] == " + in.next() + " && in[6] == " + in.next() + ") {\n\n" + makeCase() + tab(--t) + "}\n\n";
		}
		
		if("CASEFULLCMD".equals(cmd.toUpperCase())) {
			return tab(t++) + "if(in[7] == " + in.next() + " && in[6] == " + in.next() +
					" && (in[5] == " + in.next() + " && in[4] == " + in.next() + 
					" && (in[3] == " + in.next() + " && in[2] == " + in.next() + 
					" && (in[1] == " + in.next() + " && in[0] == " + in.next() + ") {\n\n" + makeCase() + tab(--t) + "}\n\n";
		}
		
		if("END.".equals(cmd.toUpperCase())) {
			if(loop) return "}\n";
			return null;
		}
		
		if("TAKE".equals(cmd.toUpperCase())) {
			return tab(t) + "takeFigure();\n\n";
		}
		
		if("PUT".equals(cmd.toUpperCase())) {
			return tab(t) + "putFig();\n\n";
		}

		if("CLOCK".equals(cmd.toUpperCase())) {
			return 	tab(t) + "moveToPoint(CLOCK_u);\n" +
					tab(t) + "moveToPoint(CLOCK_l);\n" +
					tab(t) + "moveToPoint(CLOCK_u);\n";
		}

		if("HOME".equals(cmd.toUpperCase())) {
			return tab(t) + "moveToPoint(HOME);\n";
		}
		
		if("MOVE".equals(cmd.toUpperCase())) {
			return move(in.next());
		}
		
		return "";
	}
	
	private Scanner in; //Этот объект занимается чтением с консоли
	private boolean loop = false; //Эта переменная оперделяет, нужно ли в конце программы закрывать бесконеченый цикл
	private int t = 0; //Текущий уровень табуляции
	
	//Этот метод читает входящую программу и генерирует программу для робота
	public void generateOutput() {
		in = new Scanner(System.in);
		out = "";
		loop = false;
		
		while(true) {
			String str = checkCommand(in.next());
			if(str != null) {
				out += str;
				if("}\n".equals(str.toUpperCase())) break;
			} else break;
		}
		
		System.out.println(out);
		
		in.close();
	}
	
	//Генерирует всякого рода движения
	private String move(String arg) {
		String s = "";
		
		if("OUT".equals(arg.toUpperCase())) {
			return tab(t) + "moveToPoint(BOX);\n\n";
		}
		
		for(int i=0; i<64; ++i) {
			s += tab(t) + "if(in[5] == " + (i>>5)%2 + 
						 " && in[4] == " + (i>>4)%2 +
						 " && in[3] == " + (i>>3)%2 + 
						 " && in[2] == " + (i>>2)%2 +
						 " && in[1] == " + (i>>1)%2 +
						 " && in[0] == " + (i>>0)%2 + ") {\n" + 
			("UP".equals(arg.toUpperCase()) ? tab(t+1) + "moveToPoint(" + decodeField(i) + "_l);\n" + tab(t+1) + "moveToPoint("+decodeField(i)+"_u);\n" : 
							  tab(t+1) + "moveToPoint(" + decodeField(i) + "_u);\n" + tab(t+1) + "moveToPoint("+decodeField(i)+"_l);\n") +
							  tab(t) + "}\n\n";
		}
		
		return s;
	}
	
	//Преобразует номер поля 0..63 в более привычную форму
	private String decodeField(int i) {
		return (char)('A' + (i%8)) + "" + (char)('1' + (i>>3));
	}
	
	//обработка блока с условием
	private String makeCase() {
		String s = "";
		String cmd = in.next();
		
		while(!"ENDCASE".equals(cmd.toUpperCase())) {
			s += checkCommand(cmd);
			cmd = in.next();
		}
		
		return s;
	}

}
