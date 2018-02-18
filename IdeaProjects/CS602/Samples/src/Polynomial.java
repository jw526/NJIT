//package edu.njit.cs602.samples;

/**
 * Created by Ravi Varadarajan on 1/30/2018.
 */
public class Polynomial {

    private static final int MAXSIZE = 50;
    private final double [] coeffArr;

    public Polynomial(double coeff, int power) {
        this.coeffArr = new double[MAXSIZE];
        this.coeffArr[power] = coeff;
    }

    public Polynomial(double coeff) {
        this(coeff, 0);
    }

    public Polynomial(double [] coeffArr) {
        int maxSize = coeffArr.length < MAXSIZE ? coeffArr.length : MAXSIZE;
        this.coeffArr = new double[MAXSIZE];
        for (int i=0; i < maxSize; i++) {
            this.coeffArr[i] = coeffArr[i];
        }
    }

    public Polynomial add(Polynomial p) {
        Polynomial result = new Polynomial(0);
        double [] coeffArr = result.coeffArr;
        Polynomial p1 = (Polynomial) p;
        for (int i=0; i < MAXSIZE; i++) {
            coeffArr[i] = this.coeffArr[i] + p1.coeffArr[i];
        }
        return result;
    }


    public double evaluate(double x) {
        double sum = 0.0;
        double powerX = 1.0;
        for (int i=0; i < MAXSIZE; i++) {
            sum += (this.coeffArr[i] * powerX);
            powerX *= x;
        }
        return sum;
    }

    public String toString() {
        StringBuilder bld = new StringBuilder();
        for (int i=MAXSIZE-1; i >= 0; i--) {
            if (coeffArr[i] != 0) {
                if (bld.length() > 0 && coeffArr[i] > 0) {
                    // not the first term and positive term
                    bld.append("+");
                }
                bld.append(coeffArr[i]);
                if (i > 1) {
                    // display for non-zero powers
                    bld.append("x^");
                    bld.append(i);
                } else if (i > 0) {
                    bld.append("x");
                }
            }
        }
        return bld.toString();
    }

    public static void main(String [] args) {
        Polynomial p1 = new Polynomial(5.5);
        System.out.println("p1(x)=" + p1.toString());
        Polynomial p2 = new Polynomial(-2.3, 4);
        System.out.println("p2(x)=" + p2.toString());
        System.out.println(("p1(x)+p2(x)=" +p1.add(p2).toString()));
        Polynomial p3 = new Polynomial(new double [] {0, -3.4, 0, 5});
        System.out.println("p3(x)=" + p3.toString());
        System.out.println("p3(2)=" + p3.evaluate(2));
    }
}
