/**
 *
 *  Author:         Manas Gupta
 *  Description:    This java file uses Booth's algorithm to multiply 2 binary numbers (in 2's complement).
 *                  Input:  In decimal format
 *                  Output: both Binary and Decimal values
 */
package booth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class boothsAlgorithm {

    public static void main(String[] args) {
        Scanner sc = new Scanner(new BufferedReader(new InputStreamReader(System.in)));

        System.out.println("\nWelcome to BoothWare    " + "*_*\n");

        // RECEIVE INPUT
        System.out.println("Enter Multiplicand: ");
        long a=sc.nextLong();    // multiplicand
        System.out.println("Enter Multiplier: ");
        long b=sc.nextLong();    //multiplier
        sc.nextLine();

        boolean display=false;
        System.out.println("\nWould you like to see the steps involved in the computation ?");
        System.out.println("yes / no    (case sensitive)");

        String decision = sc.nextLine();
        if(decision.equals("yes"))
            display = true;

        // decide the size of the registers to use
        int size;
        if(a == 0 && b == 0)
            size = 1;
        else
            size = (int)(Math.log(Math.max(Math.abs(a),Math.abs(b)))/Math.log(2)+ 1e-11)+1;
        //additional bit for sign at index 0
        size++;

        String tmp="";
        for(int i=0;i<size;i++){
            tmp+="0";
        }

        // INITIALIZATIONS
        StringBuilder ac = new StringBuilder(tmp);
        StringBuilder p= new StringBuilder("0");
        StringBuilder q = toBinary(b,size);
        StringBuilder m = toBinary(a,size);
        StringBuilder complement = twos(m);
        int count = size;

        tmp="";
        for(int i=0;i<size-2;i++){
            tmp+=" ";
        }

        if(display){
            System.out.println("\nInitial values");
            System.out.println("AC"+tmp+"\t\t"+"Q "+tmp+"\t\t"+"P "+tmp);
            System.out.println(ac+ "\t\t"+ q+ "\t\t"+ p+"\n");
        }


        while(count>0){

            /**
             *      Main Booth's Algorithm
             */
            if(q.charAt(size-1)=='1' && p.charAt(0)=='0')
                // ac = ac - m
                ac=add(ac,complement);

            else if(q.charAt(size-1)=='0' && p.charAt(0)=='1')
                // ac = ac + m
                ac=add(ac,m);

            arithmeticShiftRight(ac,q,p);
            count--;

            if(display)
                System.out.println(ac + "\t\t"+ q + "\t\t" + p + "\t\tCycle "+ (size-count));
        }

        StringBuilder res= new StringBuilder(ac+""+q);
        long val;

        System.out.println("\nResultant Binary Number: "+ res);

        // check sign bit
        if(res.charAt(0)=='1'){
            res = twos(res);
            val = toDecimal(res);
            val = -val;
        }
        else
            val=toDecimal(res);

        System.out.println(a+"\tx\t"+b+"\t=\t"+val);

    }

    /**
     *
     * @param ac    intermediate binary numbers stored by accumulator
     * @param q     intermediate values of multiplier
     * @param p     bit value stored in p
     */
    static void arithmeticShiftRight(StringBuilder ac,StringBuilder q,StringBuilder p){
        p.setCharAt(0,q.charAt(q.length()-1));
        for(int i=q.length()-1;i>0;i--){
            q.setCharAt(i,q.charAt(i-1));
        }
        q.setCharAt(0,ac.charAt(ac.length()-1));
        for(int i=ac.length()-1;i>0;i--){
            ac.setCharAt(i,ac.charAt(i-1));
        }
    }

    /**
     *
     * @param a     first binary number
     * @param b     second binary number
     * @return      a+b in binary format
     */
    static StringBuilder add(StringBuilder a, StringBuilder b){
        int size = a.length();
        StringBuilder tmp = new StringBuilder(size);
        int carry = 0;

        while(size>0){
            tmp.append(((int) a.charAt(size - 1) - 48 + (int) b.charAt(size - 1) - 48 + carry) % 2);
            if((int)a.charAt(size - 1)-48+(int)b.charAt(size-1)-48+carry>1)
                carry=1;
            else
                carry=0;
            size--;
        }
        // adding from right to left but appending from left to right, so reversing
        return tmp.reverse();
    }

    /**
     *
     * @param s     binary string
     * @return      return 2's complement of s
     */
    static StringBuilder twos(StringBuilder s){
        StringBuilder tmp = new StringBuilder(s);
        int size= tmp.length();

        // take 1's complement
        while(size>0){
            if(tmp.charAt(size-1)=='1')
                tmp.setCharAt(size-1,'0');
            else
                tmp.setCharAt(size-1,'1');
            size--;
        }

        // add 1 to 1's complement
        size= tmp.length();
        while(size>0){
            if(tmp.charAt(size-1)=='1')
                tmp.setCharAt(size-1,'0');
            else{
                tmp.setCharAt(size-1,'1');
                break;
            }
            size--;
        }
        return tmp;
    }

    /**
     *
     * @param s     Binary Representation to be converted to decimal format
     * @return      Decimal Value
     */
    static long toDecimal(StringBuilder s){
        long sm=0;
        long tmp=1;

        for(int i=s.length()-1;i>=0;i--){
            if(s.charAt(i)=='1')
                sm+=tmp;
            tmp<<=1;
        }
        return sm;
    }

    /**
     *
     * @param n     the number whose binary representation is needed
     * @param size  number of bits required to store n
     * @return      Binary representation of n
     */
    static StringBuilder toBinary(long n,int size){
        StringBuilder tmp = new StringBuilder(size);
        boolean flg=false;

        if(n<0) {
            flg = true;
            n = Math.abs(n);
        }

        while(n!=0){
            tmp.append(n % 2);
            n>>=1;
        }

        while(tmp.length()<size){
            tmp.append("0");
        }

        tmp.reverse();
        if(flg)
            tmp = twos(tmp);
        return tmp;
    }

}//class end
