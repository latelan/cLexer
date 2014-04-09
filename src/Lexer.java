import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

	private static char ch; 
	private static String strToken; 
	private static int index = 0; // 列号
	private static int line = 1;  // 行号
	private Map<Integer,String> keywords; // 关键字表
	private HashMap<String, Integer> punctuations; // 运算符和分隔符表
	private static ArrayList<String> tokenlist = new ArrayList<String>(); // 词法分析结果表
	private static ArrayList<String> p = new ArrayList<String>(); // 标示符表
	private static ArrayList<String> q = new ArrayList<String>(); // 常量表

	// get and set 函数
	public char getCh() {
		return ch;
	}

	public void setCh(char ch) {
		Lexer.ch = ch;
	}

	public String getStrToken() {
		return strToken;
	}

	public void setStrToken(String strToken) {
		Lexer.strToken = strToken;
	}

	public void setPunctuations(HashMap<String, Integer> punctuations) {
		this.punctuations = punctuations;
	}

	public Map<String, Integer> getPunctuations() {
		return punctuations;
	}

	public void setKeywords(Map<Integer, String> keywords) {
		this.keywords = keywords;
	}

	public Map<Integer, String> getKeywords() {
		return keywords;
	}

	public void init(String keywordPath, String punctuationPath) {
		File file = null;
		BufferedReader reader = null;
		Map<Integer, String> tempKeywords = new HashMap<Integer, String>();
		HashMap<String, Integer> tempPunctuations = new HashMap<String, Integer>();
		try{
			file = new File(keywordPath);
			reader = new BufferedReader(new FileReader(file));
			String tempStr = null;
			while ((tempStr = reader.readLine()) != null) { // 读取关键字表
				int n = tempStr.indexOf(' ');
				String str1 = tempStr.substring(0,n);
				String str2 = tempStr.substring(n+1,tempStr.length());
				tempKeywords.put(Integer.parseInt(str1),str2);
			}
			setKeywords(tempKeywords);

			tempStr = null;
			file = new File(punctuationPath);
			reader = new BufferedReader(new FileReader(file));
			while((tempStr = reader.readLine()) != null ) { // 读取标号表
				int n = tempStr.indexOf(' ');
				String str1 = tempStr.substring(0,n);
				String str2 = tempStr.substring(n+1,tempStr.length());
				tempPunctuations.put(str2, Integer.parseInt(str1));
			}
			setPunctuations(tempPunctuations);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public void saveToFile(String idPath, String cntPath, String tklstPath)
	{
		FileWriter fw = null;
		try{
			fw = new FileWriter(tklstPath);
			for (String token : tokenlist) {
				fw.write(token + "\n");
			}
			fw.close();

			fw = new FileWriter(idPath);
			for (String id : p) {
				fw.write(id + "\n");
			}
			fw.close();

			fw = new FileWriter(cntPath);
			for (String cnt : q) {
				fw.write(cnt + "\n");
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 构造函数
	public Lexer() {
		this.keywords = new HashMap< Integer,String >();
		this.punctuations = new HashMap<String, Integer>();
	}

	// 函数定义（词法分析函数）
	public boolean Analyse(char[] strArray) {
		char temp1;
		int rowLength = strArray.length;
		index = 0; // 每次分析一行完成后就将index置0
		while (index < rowLength) {
			strToken = "";
			ch = GetChar(strArray);
			//System.out.println(strArray);

			// TODO
			if (ch == ',') {
				tokenlist.add("(81,,)");
			}
			else if (ch == '{') {
				tokenlist.add("(82,{) ");
			}
			else if (ch == '}') {
				tokenlist.add("(83,}) ");
			}
			else if (ch == '(') {
				tokenlist.add("(40,() ");
			}
			else if (ch == ')') {
				tokenlist.add("(41,)) ");
			}
			else if (ch == '[') {
				tokenlist.add("(42,[) ");
			}
			else if (ch == ']') {
				tokenlist.add("(43,]) ");
			}
			else if (ch == '#') {
				tokenlist.add("(75,#) ");
			}
			else if (ch == '\"') {
				tokenlist.add("(85,\") ");
			}
			else if (ch == '\'') {
				tokenlist.add("(86,\') ");
			}
			else if (ch == '\\') {
				tokenlist.add("(89,\\)");
			}
			else if (ch == ';') {
				tokenlist.add("(88,;) ");
			}
			else if(ch==':') {
				tokenlist.add("(80,:)");
			}
			else if(ch=='.') {
				tokenlist.add("(87,.)");
			}
			else if (ch == '=') {
				if (( temp1=this.getNextChar(strArray)) == '=') {
					tokenlist.add("(63,==)");
				}else {
					index--;
					tokenlist.add("(70,=)");
				} 	
			}
			else if (ch == '+') {
				if ((temp1=this.getNextChar(strArray)) == '+') {
					tokenlist.add("(48,++)");
				}else {
					index--;
					tokenlist.add("(55,+)");
				}
			}
			else if (ch == '-') {
				temp1 = this.getNextChar(strArray);
				if (temp1 == '-') {
					tokenlist.add("(49,--)");
				}else if (temp1 == '=') {
					tokenlist.add("(72,-=)");
				}else if (temp1 >= '0' && temp1 <= '9') {
					// 识别常量
					// eg: -3.14
					// TODO
				}else{
					index--;
					tokenlist.add("(50,-)");
				}
			}
			else if (ch == '*') {
				if ((temp1 = this.getNextChar(strArray)) == '=') {
					tokenlist.add("(73,*=)");
				}else {
					index--;
					tokenlist.add("(51,*)");
				}
			}
			else if (ch == '/') {
				if ((temp1 = this.getNextChar(strArray)) == '=') {
					tokenlist.add("(74,/=)");
				}else {
					index--;
					tokenlist.add("(53,/)");
				}
			}
			else if (ch == '%') {
				if ((temp1 = this.getNextChar(strArray)) == '=') {
					tokenlist.add("(75,%=)");
				}else {
					index--;
					tokenlist.add("(54,%)");
				}
			}
			else if (ch == '>') {
				temp1 = this.getNextChar(strArray);
				if (temp1 == '>') {
					tokenlist.add("(58,>>)");
				}else if (temp1 == '=') {
					tokenlist.add("(62,>=)");
				}else{
					index--;
					tokenlist.add("(61,>)");
				}
			}
			else if (ch == '<') {
				temp1 = this.getNextChar(strArray);
				if (temp1 == '<') {
					tokenlist.add("(58,>>)");
				}else if (temp1 == '=') {
					tokenlist.add("(60,<=)");
				}else{
					index--;
					tokenlist.add("(59,<)");
				}
			}
			else if (ch == '!') {
				if ((temp1 = this.getNextChar(strArray)) == '=') {
					tokenlist.add("(64,!=)");
				}else {
					index--;
					tokenlist.add("(46,!)");
				}
			}
			else if (ch == '&') {
				temp1 = this.getNextChar(strArray);
				if (temp1 == '&') {
					tokenlist.add("(67,&&)");
				}else if (temp1 == '=') {
					tokenlist.add("(78,&=)");
				}else{
					index--;
					tokenlist.add("(52,&)");
				}
			}
			else if (ch == '|') {
				temp1 = this.getNextChar(strArray);
				if (temp1 == '|') {
					tokenlist.add("(68,||)");
				}else if (temp1 == '=') {
					tokenlist.add("(80,|=)");
				}else{
					index--;
					tokenlist.add("(66,|)");
				}
			}
			else if (ch == '?') {
				// 检索三目运算符
				// TODO
				System.out.println("(69,TODO)");
			}
			else if (java.lang.Character.isLetter(ch)) 
			{// 识别标示符
				strToken = contact(strToken, ch);
				ch = getNextChar(strArray);
				while ((java.lang.Character.isLetter(ch))
						|| (java.lang.Character.isDigit(ch)
						|| ch == '_')) {
					strToken = contact(strToken, ch);
					ch = getNextChar(strArray);
							}
				index--;
				if (findKeyword(strToken)) 
				{// 识别关键字
					int i = getKeyWordKey(strToken);
					tokenlist.add("("+i+"," + strToken +")");
				} else { // 保存标示符
					if(!exist(p,strToken)){
						p.add(strToken);
					}
					int i = getindex(p,strToken);
					tokenlist.add("(33,"+i+")");  	
				}
			}
			else if (java.lang.Character.isDigit(ch)) 
			{// 识别常量
				boolean pointTag = false; // 小数点标记
				strToken = this.contact(strToken, ch);
				ch = this.getNextChar(strArray);
				while (java.lang.Character.isDigit(ch)
						|| ch =='.') {
					if (ch == '.' )
					{
						if(pointTag == true) {
							break; // ch 为小数点，且之前发现小数点，则停止分析
						}else {
							pointTag = true;
						}
					}
					strToken = this.contact(strToken, ch);
					ch = this.getNextChar(strArray);
				}
				if (ch == '.' && pointTag == true) {
					return true; // ch 为小数点，且之前发现小数点，则出错
				}
				index--;
				if(!exist(q,strToken)){
					q.add(strToken);
				}
				int i=getindex(q,strToken);	
				tokenlist.add("(34,"+i+")");
				strToken = "";
			} 
			else 
			{// 其他符号
				String key = String.valueOf(ch);
				if (this.findPunctuation(key)) {
					System.out.println("("+this.getPunctuation(key)+","
							+ key + ")");
				} else if (key.equals(" ") || key.equals("\t")) {
					continue;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public char GetChar(char[] array) {
		try {
			while ((array[index]) == ' ') { 
				index++;
			}
			index++;	// 提前指向下一个字符
		} catch (ArrayIndexOutOfBoundsException e) {
			return ' ';
		}
		return array[index - 1];
	}

	public char getNextChar(char[] strChar) {
		try {
			index++;
			return strChar[index - 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return ' ';
		}
	}

	public String contact(String token, char ch) {
		return token + String.valueOf(ch);
	}

	public boolean findKeyword(String str) {
		for(int i=1;i<=32;i++){
			if(str.equalsIgnoreCase(this.keywords.get(i)))
				return true;
		}
		return false;	
	}

	public boolean findPunctuation(String str) {
		if (this.punctuations.containsKey(str)) {
			return true;
		} else
			return false;
	}

	public int getPunctuation(String str) {
		return this.punctuations.get(str);
	}

	public void callError(int line) {
		System.out.println("出现错误，错误位置在第" + line + "行,第" + index + "列");
	}

	public boolean exist(ArrayList<String> p,String strToken)
	{
		if(p.contains(getStrToken()))
			return true;
		else
			return false;
	}

	public int getKeyWordKey(String str)
	{
		for(int i=1;i<=32;i++)
		{
			if(str.equalsIgnoreCase(this.keywords.get(i)))
				return i;
		}
		return -1;
	}

	public int getindex(ArrayList<String> p,String Str)
	{
		return p.lastIndexOf(Str) + 1;
	}

	public static void main(String args[]) {
		if(args.length != 3){
			System.out.println("Error: Too few arguments");
			return ;
		}
		String filename = args[2];
		if (filename.indexOf('.') == -1) {
			System.out.println("Error: Need a *.c source file");
			return;
		}

		String name = filename.substring(0,filename.indexOf('.'));
		//System.out.println("Name: " + name);
		File file = new File(filename);
		Lexer lex = new Lexer();
		char[] strChar = new char[100];// 限制每行代码字符数不超过100
		BufferedReader reader = null;
		try {
			lex.init(args[0],args[1]);
			//System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			boolean errorTag = false;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				// System.out.println("line " + line + ": " + tempString);
				strChar = tempString.toCharArray();
				errorTag = lex.Analyse(strChar);
				if (errorTag == false)
					line++;
				else {
					lex.callError(line);
					break;
				}
			}
			if (errorTag == false) {
				System.out.println("词法分析结果");
				for (String token : tokenlist) {
					System.out.println(token);
				}
				System.out.println("标示符表");
				System.out.println(p.toString());
				System.out.println("常数表");
				System.out.println(q.toString());
				// 保存上面三者到文件
				lex.saveToFile(name+".id", name+".cnt", name+".tklst");
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
