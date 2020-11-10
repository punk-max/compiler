import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class sfyxfx {
    public static int flag=0;

    public static boolean isVt(char c){//判断字符是否为终结符
        return (c == '+' || c == '*' || c == 'i' || c == '(' || c == ')');
    }

    public static boolean isEnd(char c){//判断是否为终止符号
        return (c == '\r' || c == '\n');
    }

    public static int Compare(char c1,char c2){//比较优先级，大于为3，等于为2，小于为1，无法比较为0
        if (c1 == '+')
        {
            switch(c2){
                case '+':
                    return 3;
                case '*':
                    return 1;
                case 'i':
                    return 1;
                case '(':
                    return 1;
                case ')':
                    return 3;
                default:
                    return 3;
            }
        }
        else if(c1 == '*'){
            switch(c2){
                case '+':
                    return 3;
                case '*':
                    return 3;
                case 'i':
                    return 1;
                case '(':
                    return 1;
                case ')':
                    return 3;
                default:
                    return 3;
            }
        }
        else if(c1 == 'i'){
            switch(c2){
                case '+':
                    return 3;
                case '*':
                    return 3;
                case 'i':
                    return 0;
                case '(':
                    return 0;
                case ')':
                    return 3;
                default:
                    return 3;
            }
        }
        else if(c1 == '('){
            switch(c2){
                case '+':
                    return 1;
                case '*':
                    return 1;
                case 'i':
                    return 1;
                case '(':
                    return 1;
                case ')':
                    return 2;
                default:
                    return 0;
            }
        }
        else if(c1 == ')'){
            switch(c2){
                case '+':
                    return 3;
                case '*':
                    return 3;
                case 'i':
                    return 0;
                case '(':
                    return 0;
                case ')':
                    return 3;
                default:
                    return 3;
            }
        }
        else if(isEnd(c1)){
            switch(c2){
                case '+':
                    return 1;
                case '*':
                    return 1;
                case 'i':
                    return 1;
                case '(':
                    return 1;
                case ')':
                    return 0;
                default:
                    return 0;
            }
        }
        else
            return 0;
    }

    public static void main(String[] args) {
        File file = new File(args[0]);
        Stack stack1 = new Stack();//存储符号栈
        Stack stack2 = new Stack();//存储终结符栈
        char c;
        int i;
        stack2.push('\r');
        try {
            FileReader fileReader = new FileReader(file);
            while((i=fileReader.read())!=-1)
            {
                c = (char) i;
                if((!isVt(c) && !isEnd(c)) || Compare((char)stack2.peek(),c) == 0)//对于不能识别或无法比较符号优先关系的栈顶和读入符号，输出一行E
                {
                    System.out.println("E");
                    System.exit(0);
                }
                while(Compare((char)stack2.peek(),c) > 1)//如果栈顶的终结符优先级大于等于读入符号
                {
                    //进行规约
                    if((char)stack2.peek() == 'i')//符号栈栈顶为i，直接规约成E
                    {
                        stack1.pop();
                        stack1.push('E');
                        stack2.pop();
                        System.out.println("R");
                    }
                    else if((char)stack2.peek() == '+' || (char)stack2.peek() == '*')//终结符为+或*时两边都为E时可以规约
                    {
                        if((char)stack1.peek() != 'E')
                        {
                            System.out.println("RE");
                            System.exit(0);
                        }
                        else{
                            stack1.pop();
                            stack1.pop();
                            if((char)stack1.peek() != 'E')
                            {
                                System.out.println("RE");
                                System.exit(0);
                            }
                            else
                            {
                                stack2.pop();
                                System.out.println("R");
                            }
                        }
                    }
                    else {//由优先矩阵可以看到读入为左括号时优先级均为小于，故此处只能是右括号
                        if((char)stack1.peek() != 'E')
                        {
                            System.out.println("RE");
                            System.exit(0);
                        }
                        else{
                            stack1.pop();
                            if((char)stack1.peek() != '(')
                                System.out.println("RE");
                            else
                                System.out.println("R");
                        }
                    }
                }
                if(Compare((char)stack2.peek(),c) == 1) {//如果栈顶的终结符优先级小于读入符号，入栈
                    stack1.push(c);
                    stack2.push(c);
                    System.out.println("I" + c);
                }
                if (isEnd(c) && isEnd((char)stack2.peek()))//栈中无终结符
                {
                    stack1.pop();
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
