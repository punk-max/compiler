import java.io.*;

public class cffx {
        public static String[] key ={"BEGIN","END","FOR","IF","THEN","ELSE",};
        public static String[] outputKey = {"Begin","End","For","If","Then","Else"};
        public static String[] symbol = { ":","+","*",",","(",")",":="};
        public static String[] outputSymbol = {"Colon","Plus","Star","Comma","LParenthesis","RParenthesis","Assign"};
        public static int flag=0;

        public static boolean isBlank(char c) {//判断是否为空格、换行符和制表符
            if (c == ' ' || c == '\n' || c == '\t' || c == '\r')
                return true;
            else
                return false;
        }

        public static boolean isDigit(char c){
            if (c>='0' && c<='9')
                return true;
            return false;
        }

        public static boolean Symbol(char c){//判断是否为单个符号
            if (c == ':' || c == '+' || c == '*' || c == ',' || c == '(' || c == ')' || c == '=')
                return true;
            else
                return false;
        }

        public static boolean isLetter(char c){//判断是否为字母
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                return true;
            return false;
        }

        public static int isSymbol(String s) { //判断运算符和界符
            int i;
            for (i = 0; i < 7; i++) {
                if (s.equals(symbol[i]))
                    return i + 1;
            }
            return 0;
        }

        public static int isKeyWord(String s) {//判断是否为关键字
            int i;
            for (i = 0; i < 6; i++) {
                if (s .equals(key[i]))
                    return i + 1;
            }
            return 0;
        }

        public static boolean isKey(String s) {//判断是否为标识符
            int i;
            if (isDigit(s.charAt(0)))
                return false;
            if(s.length()>=1){
                for (i = 1; i < s.length(); i++)
                {
                    if (!isDigit(s.charAt(i)) && !isLetter(s.charAt(i)))
                        return false;
                }
            }
            return true;
        }

        public static boolean isLegal(String s){//简单判断字符串是否合法
            int i;
            for (i = 0; i < s.length(); i++) {
                if (!isDigit(s.charAt(i)) && !isLetter(s.charAt(i)))
                    return false;
            }
            return true;
        }

    public static String[] divide(String s) {//处理首字符为数字的字符串
        int i = 0;
        String str[] =new String[2];
        while (i < s.length())
        {
            if (!isDigit(s.charAt(i)))
            {
                str[0] = s.substring(0,i);
                str[1] = s.substring(i);
                break;
            }
            i++;
        }
        //System.out.println(str[0]+" "+str[1]);
        return str;
    }

    public static boolean isNumeric(String str) {//判断是否数字
        for (int i = 0; i < str.length(); i++) {
            if (!isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String toNum(String str) {//转化为非零数字
            if (str.length()==1)
                return str;
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i)!='0')
                return str.substring(i);
            if(i==str.length()-1)
                return "0";
        }
        return str;
    }

    public static void checkStr(String s){
            if (flag == 1)
            {
                System.out.println();
                System.out.println("Colon");
                flag=0;
            }
            if(isLegal(s))
            {
                if(isKeyWord(s)!=0){
                    System.out.println(outputKey[isKeyWord(s)-1]);
                }
                else if(isKey(s))
                {
                    System.out.println("Ident("+s+")");
                }
                else if(isNumeric(s))
                {
                    System.out.println("Int("+ toNum(s) + ")");
                }
                else{
                    String[] str = divide(s);
                    System.out.println("Int(" + toNum(str[0]) + ")");
                    System.out.println("Ident(" + str[1] + ")");
                }
            }
            else
            {
                System.out.println("Unknown");
                System.exit(0);
            }
    }

        public static void main(String[] args){
            File file = new File(args[0]);
            String word="";
            char c;
            int i;
            try {
                FileReader fileReader = new FileReader(file);
                while((i=fileReader.read())!=-1)
                {
                    c = (char) i;
                    if(!isBlank(c) && !Symbol(c))
                    {
                        word+=c;
                        continue;
                    }
                    else if (Symbol(c)){
                        if(word!=""&&flag==0)
                        {
                            checkStr(word);
                            word = "";
                        }
                        if(c==':'){
                            if(flag==1)
                            {
                                System.out.println("Colon");
                                word="";
                            }
                            flag=1;
                            word+=':';
                        }
                        else if(c=='='){
                            if (flag==1)
                            {
                                word+='=';
                                System.out.println(outputSymbol[isSymbol(word)-1]);
                                word="";
                                flag=0;
                            }
                            else
                            {
                                System.out.println("Unknown");
                                System.exit(0);
                            }
                        }
                        else{
                            word+=c;
                            System.out.println(outputSymbol[isSymbol(word)-1]);
                            word="";
                        }
                    }
                    else{
                        if (flag==1)
                        {
                            System.out.println("Colon");
                            flag=0;
                        }
                        if(word!="")
                        {
                            checkStr(word);
                            word = "";
                        }
                    }
                }
                if(word!="")
                {
                    checkStr(word);
                }
            }catch (IOException e) {
                e.printStackTrace();
                return;
            }
            return ;
        }
}
