/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//-12*+2--20/-4
package expression_checking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

public class Expression_Checking {
  
    public static Stack<Float> values= new Stack<>();
    public static Stack<Integer> values_length= new Stack<>();
    public static Stack<Integer> values_first_index= new Stack<>();
    public static Stack<Character> operators= new Stack<>();
    public static String expression;
    
    public void empty_stack(Stack s)
    {
        while(!s.empty())
        {
            s.pop();
        }
    }
    
    public void show_stack_content()
    {
        System.out.println("Values Stack : ");
        while(!values.empty())
        {
            System.out.println(values.pop());
        }
        
        System.out.println("Operators Stack : ");
        while(!operators.empty())
        {
            System.out.println(operators.pop());
        }
    }
    
    public float L_to_R_evaluation()
    {
        Stack<Float> temp_values= new Stack<>();
        Stack<Character> temp_operators= new Stack<>();
        while(!values.empty())
        {
            temp_values.push(values.pop());
        }
        while(!operators.empty())
        {
            temp_operators.push(operators.pop());
        }
        
        while(!temp_operators.empty())
        {
            switch(temp_operators.pop())
            {
                case '+' : temp_values.push(temp_values.pop()+temp_values.pop()); break;
                case '-' : temp_values.push(temp_values.pop()-temp_values.pop()); break;
                case '*' : temp_values.push(temp_values.pop()*temp_values.pop()); break;
                case '/' : temp_values.push(temp_values.pop()/temp_values.pop());
            }
        }
        return temp_values.pop();
    }

    public void evaluation()
    {
        empty_stack(values);
        empty_stack(values_length);
        empty_stack(values_first_index);
        
        check_expression();
        
        Stack<Float> temp_values= new Stack<>();
        Stack<Character> temp_operators= new Stack<>();
        StringBuffer temp_expression=new StringBuffer();
        
        while(!values.empty())
        {
            temp_values.push(values.pop());
            //System.out.println("temp_values" + temp_values.peek());
        }
        while(!operators.empty())
        {
            temp_operators.push(operators.pop());
            //System.out.println("temp_operator" + temp_operators.peek());
        }
        while(!temp_values.empty())
        {
            temp_expression.append(temp_values.pop());
            temp_expression.append(",");
            if(!temp_operators.empty())
            {
                temp_expression.append(temp_operators.pop());
                temp_expression.append(",");
            }
        }
        
        StringBuilder postfix_expression=new StringBuilder();
        int temp_expression_length=temp_expression.length();
        Stack<Character> op=new Stack<>();
        
        
        for(int i=0; i<temp_expression_length; )
        {
            boolean flag=true;
            char ch_next;
            char ch_next_next;
            char ch=temp_expression.charAt(i);
            postfix_expression.append(ch);
            if(i<temp_expression_length-2)
            {
                ch_next=temp_expression.charAt(i+1);
                ch_next_next=temp_expression.charAt(i+2);
            
                if(ch==',' && (ch_next=='+'||ch_next=='-'||ch_next=='*'||ch_next=='/') && ch_next_next==',')
                {
                    if(op.empty())
                    {
                        op.push(ch_next);
                    }
                    else
                    {
                        while(flag && !op.empty())
                        {
                            if(ch_next=='+'||ch_next=='-')
                            {
                                if(op.peek()=='*'||op.peek()=='/')
                                {
                                    postfix_expression.append(op.pop());
                                    postfix_expression.append(",");
                                }
                                else
                                {
                                    op.push(ch_next);
                                    flag=false;
                                }
                            }
                            else
                            {
                                if(op.peek()=='*'||op.peek()=='/')
                                {
                                    postfix_expression.append(op.pop());
                                    postfix_expression.append(",");
                                }
                                else
                                {
                                    op.push(ch_next);
                                    flag=false;
                                }
                            }
                        }
                        if(op.empty())
                        {
                            op.push(ch_next);
                        }
                    }
                    i=i+2;
                }
            }
            i++;
        }
        while(!op.empty())
        {
            postfix_expression.append(op.pop());
            postfix_expression.append(",");
        }
        System.out.println("Postfix Expression: "+postfix_expression);
        
        postfix_eval(postfix_expression);
    }
    
    public void postfix_eval(StringBuilder postfix_expression)
    {
        boolean flag0=true;
        int length=postfix_expression.length();
        Stack<Float>final_value=new Stack();
        
        for(int i=0; i<length-1; )
        {
            String temp=new String();
            while(flag0 && i<length-1)
            {
                char ch=postfix_expression.charAt(i);
                if(ch!=',')
                {
                    temp=temp+ch;
                    i++;
                }
                else
                {
                    flag0=false;
                }
            }
            flag0=true;
            int temp_length=temp.length();
            if(temp_length==1 && !(temp.charAt(0)>47 && temp.charAt(0)<58))
            {
                float a,b,c;
                switch(temp.charAt(0))
                {
                    case '+' :
                    {
                        a=final_value.pop();
                        b=final_value.pop();
                        c=b+a;
                        final_value.push(c);
                        break;
                    }
                    case '-' :
                    {
                        a=final_value.pop();
                        b=final_value.pop();
                        c=b-a;
                        final_value.push(c);
                        break;
                    }
                    case '*' :
                    {
                        a=final_value.pop();
                        b=final_value.pop();
                        c=b*a;
                        final_value.push(c);
                        break;
                    }
                    case '/' :
                    {
                        a=final_value.pop();
                        b=final_value.pop();
                        c=b/a;
                        final_value.push(c);
                    }
                }
            }
            else if(temp.charAt(0)=='-')
            {
                final_value.push(Float.parseFloat(temp.substring(1))*(-1));
            }
            else
            {
                final_value.push(Float.parseFloat(temp));
            }
            i++;
        }
        System.out.println("Result with BODMAS rule: " + final_value.pop());
    }
    
    public boolean check_expression()
    {
        char[] ch=expression.toCharArray();
        int z=1;
        boolean flag0=true;
        boolean flag1=true;
        boolean dot=false;
        int dot_index=-1;
        
        for(int i=0; i<ch.length && flag1; )
        {
            StringBuffer temp=new StringBuffer();
            while(flag0 && i<ch.length)
            {
                if((ch[i]>47 && ch[i]<58) || (ch[i]=='.'))
                {
                    if(ch[i]=='.')
                    {
                        if(!dot)
                        {
                            dot_index=i;
                            temp=temp.append(ch[i]);
                            dot=true;
                            i++;
                        }
                        else
                        {
                            flag0=false;
                            flag1=false;
                        }
                    }
                    else
                    {
                        temp=temp.append(ch[i]);
                        i++;
                    }
                }
                else
                {
                    flag0=false;
                }
            }
            dot=false;
            flag0=true;
            int temp_size=temp.length();
            
            float num=0;
            int index=0;
            if(temp_size>0 && flag1)
            {  
                int index_number=i-temp_size;
                if(z==1)
                {
                     if(index_number > 1)
                     {
                         flag1=false;
                     }
                     if(ch[0]=='*' || ch[0]=='/')
                     {
                         flag1=false;
                     }
                     z=-1;
                }
                else
                {
                    int a=index_number - (values_first_index.peek()+values_length.peek()-1)-1;
                    int b=values_first_index.peek()+values_length.peek();
                    if(a>2)
                    {
                        flag1=false;
                    }
                    else if(a==2)
                    {
                        if((ch[b]=='/'&&ch[b+1]=='/')||(ch[b]=='*'&&ch[b+1]=='*')||(ch[b]=='/'&&ch[b+1]=='*')||(ch[b]=='*'&&ch[b+1]=='/')||(ch[b]=='-'&&ch[b+1]=='/')||(ch[b]=='-'&&ch[b+1]=='*')||(ch[b]=='+'&&ch[b+1]=='/')||(ch[b]=='+'&&ch[b+1]=='*'))
                        {
                            flag1=false;
                        }
                        else
                        {
                            operators.push(ch[b]);
                        }
                    }
                    else
                    {
                        switch(ch[b])
                        {
                            case '+' : operators.push('+');break;
                            case '-' : operators.push('+');break;
                            case '*' : operators.push('*');break;
                            case '/' : operators.push('/');break;
                            default  : flag1=false;
                        }
                    }
                }
                
                if(flag1)
                {
                    if(dot_index!=-1)
                    {
                        int pow=dot_index-index_number-1;
                        for(int j=0; j<dot_index-index_number; j++)
                        {
                            num+=(temp.charAt(j)-'0')*Math.pow(10, pow--);
                        }
                        int after_dot=i-dot_index-1;
                        for(int j=1; j<=after_dot; j++)
                        {
                            num+=(temp.charAt(dot_index+j-index_number)-'0')*Math.pow(10, pow--);
                        }
                    }
                    else
                    {
                        for(int j=temp_size-1; j>=0; j--)
                        {
                            num+=(temp.charAt(index++)-'0')*Math.pow(10, j);
                        }
                    }
                    if((i-temp_size-1>=0) && ch[i-temp_size-1]=='-')
                    {
                        num=num*(-1);
                    }
                    dot_index=-1;
                    values.push(num);
                    values_length.push(temp_size);
                    values_first_index.push(index_number);
                }
            }
            i++;
        }
        if(flag1)
        {
            if(!(ch[ch.length-1]>47 && ch[ch.length-1]<58))
            {
                flag1=false;
            }
        }
        return flag1;
    }

    public static void main(String[] args) throws IOException{
        
        Expression_Checking E=new Expression_Checking();
        BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Expression : ");
        expression=reader.readLine();
        expression=expression.replaceAll("\\s+" , "");
        
        System.out.println("Your Expression : " + expression);
        
        boolean flag1=E.check_expression();
        
        if(!flag1)
        {
            System.out.println("Expression is incorrect.");
        }
        else
        {
            float result=E.L_to_R_evaluation();
            System.out.println("Result of Left to Right evaluation : " + result);
            
            E.evaluation();
        }
    }
}