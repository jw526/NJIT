import java.io.*;
import java.util.*;

public class IOTime {

	public static void main(String[] args) {
		System.out.println("Input File:");    //take an input
		Scanner in = new Scanner(System.in);
		String infilename = in.nextLine();
		
		System.out.println("Output File:" );  //take an output
		Scanner out = new Scanner(System.in);
		String outfilename = out.nextLine();
		
		//CharByChar(infilename, outfilename); // C:\cs226\TestFile01.txt
		//blockRW(infilename, outfilename);
		
		System.out.println("Mode 0 (Char By Char) or 1 (Block Read & Write):");
		Scanner mode = new Scanner(System.in);
		int m = mode.nextInt();
		if (m==0) {		
		CharByChar(infilename, outfilename);
		}
		else {
			if (m==1) {
				blockRW(infilename, outfilename);
			}
				
			else {
				System.out.println("wrong input, try again");
				}		
		} 
		
	
		
}
   static void CharByChar(String infilename, String outfilename) {
	   try{    
           FileInputStream file1=new FileInputStream(infilename);    
           PrintWriter output1 = new PrintWriter(new File(outfilename));
           int a;
           long startTime = System.currentTimeMillis();
           while((a = file1.read()) >-1) {
           	char c = (char)a;
           	output1.print(c);
           }
 
           file1.close();
           output1.close();
           System.out.println("Char By Char Time: "+(System.currentTimeMillis()-startTime)+" ms");
         }catch(Exception e){System.out.println(e);} 
   }
   
   static void blockRW(String infilename, String outfilename) {
	   try{    
           File file2=new File(infilename);    
           Scanner sc = new Scanner(file2);
           PrintWriter output2 = new PrintWriter(new File(outfilename));
           String aline;
           long startTime = System.currentTimeMillis();
           while(sc.hasNextLine()) {
           	aline = sc.nextLine();
           	output2.println(aline);
           }
           sc.close();
           output2.close();
           System.out.println("Block Read & Write Time: "+(System.currentTimeMillis()-startTime)+" ms");
         }catch(Exception e){System.out.println(e);} 
   }
}
