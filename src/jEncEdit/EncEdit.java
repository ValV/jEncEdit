/**
 * 
 */
package jEncEdit;

import java.io.*;

/**
 * @author <?>
 *
 */
public class EncEdit {
	/**
	 * 
	 */
	static final String encTable = new String(" \"(),-.:;?gёВГЗИКМНОПРСТУабвгдежзийклмнопрстуфхцчшщъыьэюя");

	public static StringBuffer inputBuffer = new StringBuffer();
	public static StringBuffer outputBuffer = new StringBuffer();
	public static StringBuffer hexBuffer = new StringBuffer();

	public EncEdit() {
		// TODO Auto-generated constructor stub
	}

	public static void readFile(String FileName) {
		try {
			DataInputStream inFile = new DataInputStream(
					new BufferedInputStream(new FileInputStream(FileName)));
			if (inFile.available() != 0) {
				inFile.read(); inFile.read();
			}
			inputBuffer = new StringBuffer();
			outputBuffer = new StringBuffer();
			hexBuffer = new StringBuffer();
			int inByte;
			while (inFile.available() != 0) {
				inByte = inFile.read(); inFile.read();
				if (inByte >= 184) // 198
					if (inByte >= 224) outputBuffer.append(EncEdit.encTable.charAt(inByte - 199));
					else {
						if (inByte > 202) outputBuffer.append(EncEdit.encTable.charAt((inByte - 199) + 12));
						else {
							if (inByte == 202) outputBuffer.append(EncEdit.encTable.charAt(16)); // "К"
							if (inByte == 200) outputBuffer.append(EncEdit.encTable.charAt(15)); // "И"
							if (inByte == 199) outputBuffer.append(EncEdit.encTable.charAt(14)); // "З"
							if (inByte == 195) outputBuffer.append(EncEdit.encTable.charAt(13)); // "Г"
							if (inByte == 194) outputBuffer.append(EncEdit.encTable.charAt(12)); // "В"
							if (inByte == 184) outputBuffer.append(EncEdit.encTable.charAt(11)); // "ё"
						}
					}
				else outputBuffer.append((char) inByte);
				inputBuffer.append((char) inByte);
				hexBuffer.append(String.valueOf(inByte) + "\t");
			}
			inFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void writeFile(String FileName) {
		try {
			DataOutputStream outFile = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(FileName)));
			outFile.writeUTF(outputBuffer.toString());
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
