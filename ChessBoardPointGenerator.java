
public class ChessBoardPointGenerator {
	
	//Генерирует 128 точек шахматной доски,
	//если точка пересечения осей координат
	//расположена левом нижнем углу доски(относительно робота)
	static void generatePoints(int field_width, int h1, int h2) {
		for(int y=0; y<8; ++y) {
			for(int x=0; x<8; ++x) {
				System.out.println(((char)('A' + x) + "" + (char)('1' + y)) + 
						"(" + (field_width*(x+0.5)) + "; "
							+ (field_width*(y+0.5)) + "; " + h1 + (");"));
				
				System.out.println(((char)('A' + x) + "" + (char)('1' + y)) + 
						"(" + (field_width*(x+0.5)) + "; "
							+ (field_width*(y+0.5)) + "; " + h2 + (");"));
			}
		}
	}
	
}
