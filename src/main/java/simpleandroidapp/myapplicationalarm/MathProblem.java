package simpleandroidapp.myapplicationalarm;
/**
 * Created by Jelord Rey Gulle on 10/9/2015. Java class ni para mo create og math problems to solve
 * with morethan 2 possible operators like pwede mag dungan ang (-) og (+) plus random variables
 * sample 5(random var) -(operator) 2 /(operator) 3(random var)
 * tas gena swap ang variables para maka avoid tag negative result
 * naa sad dere nga java class gena generate kung pila ka problem emo e solve
 * base sa emo ge set sa pag set og alarm
 */

import java.util.Random;


public class MathProblem
{

    private static final int ADDITION = 0;
    private static final int SUBTRACTION = 1;
    private static final int MULTIPLICATION = 2;
    private static final int DIVISION = 3;


    private int[] vars = new int[3];
    private int[] ops = new int[2];

    private int ans = 0;
    private int temp = 0;


    private boolean firstHalfSolved = false;
    private boolean secondHalfSolved = false;


    private int divideWarning;

    private String finalEquation = "";


    public MathProblem()
    {
        generateProblem();


        solve();

        finalEquation = toString();


    }

    public MathProblem(int x, int y, int z, int op1, int op2) {
        vars[0] = x;
        vars[1] = y;
        vars[2] = z;

        ops[0] = op1;
        ops[1] = op2;

        solve();

        finalEquation = toString();
    }


    private void generateProblem()
    {
        divideWarning = 0;

        Random rand = new Random();


        for(int i = 0; i < 3; i++)
        {
            vars[i] = rand.nextInt(12) + 1;
        }


        for(int i = 0; i < 2; i++)
        {
            ops[i] = rand.nextInt(3) + 1;
        }
    }


    private void solve()
    {
        firstHalfSolved = false;
        secondHalfSolved = false;


        if(ops[0] < MULTIPLICATION && ops[1] >= MULTIPLICATION)
        {
            solveSecondHalf();
        }
        else
        {
            solveFirstHalf();
        }
    }


    private void solveFirstHalf()
    {
        System.out.println(vars[0] + " "  + vars[1] + " " + vars[2]);
        System.out.println(ops[0] + " " + ops[1]);

        System.out.println("solving first half");

        switch(ops[0])
        {
            case ADDITION:
            {
                if(secondHalfSolved)
                {
                    ans = add(vars[0], ans);
                    firstHalfSolved = true;
                }
                else
                {
                    ans = add(vars[0], vars[1]);
                    firstHalfSolved = true;
                    solveSecondHalf();
                }
            }break;

            case SUBTRACTION:
            {

                if(secondHalfSolved)
                {

                    if(vars[0] < ans)
                    {
                        temp = vars[0];
                        vars[0] = vars[1];
                        vars[1] = vars[2];
                        vars[2] = temp;

                        temp = ops[0];
                        ops[0] = ops[1];
                        ops[1] = temp;


                        solve();
                    }
                    else
                    {
                        ans = subtract(vars[0], ans);
                        firstHalfSolved = true;
                    }
                }
                else
                {

                    if(vars[0] < vars[1])
                    {
                        temp = vars[0];
                        vars[0] = vars[1];
                        vars[1] = temp;
                    }
                    ans = subtract(vars[0], vars[1]);
                    firstHalfSolved = true;
                    solveSecondHalf();
                }
            }break;

            case MULTIPLICATION:
            {
                ans = multiply(vars[0], vars[1]);
                firstHalfSolved = true;
                solveSecondHalf();
            }break;

            case DIVISION:
            {

                if(vars[0] < vars[1])
                {
                    temp = vars[0];
                    vars[0] = vars[1];
                    vars[1] = temp;
                }


                while(vars[0] % vars[1] > 0)
                {
                    System.out.println("correcting");
                    if((vars[1] < 12) && (vars[1] != vars[0]))
                    {
                        vars[1]++;
                    }
                    else
                        vars[1] = 1;
                }

                ans = divide(vars[0], vars[1]);
                System.out.println(ans);
                firstHalfSolved = true;

                if(divideWarning < 2)
                    solveSecondHalf();
                else
                {
                    generateProblem();
                    solve();
                }
            }break;

        }



    }


    private void solveSecondHalf()
    {
        System.out.println(vars[0] + " "  + vars[1] + " " + vars[2]);
        System.out.println(ops[0] + " " + ops[1]);

        System.out.println("solving second half");

        switch(ops[1])
        {
            case ADDITION:
            {
                ans = add(ans, vars[2]);
                secondHalfSolved = true;
            }break;

            case SUBTRACTION:
            {

                if(ans < vars[2])
                {

                    if(vars[0] + vars[1] <= vars[2])
                    {
                        temp = vars[2];
                        vars[2] = vars[1];
                        vars[1] = vars[0];
                        vars[0] = temp;

                        temp = ops[0];
                        ops[0] = ops[1];
                        ops[1] = temp;

                        solve();
                        break;
                    }
                    else
                        vars[2] = 0;
                }
                ans = subtract(getAns(), vars[2]);
                secondHalfSolved = true;
            }break;

            case MULTIPLICATION:
            {
                if(firstHalfSolved)
                {
                    ans = multiply(ans, vars[2]);
                    secondHalfSolved = true;
                }
                else
                {
                    ans = multiply(vars[1], vars[2]);
                    secondHalfSolved = true;
                    solveFirstHalf();
                }
            }break;

            case DIVISION:
            {
                if(firstHalfSolved)
                {

                    if(ans < vars[2])
                    {
                        temp = vars[2];
                        vars[2] = vars[1];
                        vars[1] = vars[0];
                        vars[0] = temp;

                        temp = ops[0];
                        ops[0] = ops[1];
                        ops[1] = temp;

                        divideWarning++;
                        solve();
                    }
                    else
                    {

                        while(ans % vars[2] > 0)
                        {
                            if((vars[2] < 12) && (vars[2] < getAns()))
                            {
                                vars[2]++;
                            }
                            else
                                vars[2] = 1;
                        }

                        ans = divide(ans, vars[2]);
                        secondHalfSolved = true;
                    }
                }

                else
                {

                    if(vars[1] < vars[2])
                    {
                        temp = vars[1];
                        vars[1] = vars[2];
                        vars[2] = temp;
                    }


                    while(vars[1] % vars[2] > 0)
                    {
                        if((vars[2] < 12) && (vars[2] < vars[1]))
                            vars[2]++;
                        else
                            vars[2] = 1;
                    }
                    ans = divide(vars[1], vars[2]);
                    secondHalfSolved = true;
                    solveFirstHalf();
                }
            }break;
        }
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();


        if(ops[0] >= MULTIPLICATION && ops[1] >= MULTIPLICATION)
        {
            sb.append("(");
        }

        sb.append(vars[0]);


        switch (ops[0])
        {
            case ADDITION:
                sb.append(" + ");
                break;
            case SUBTRACTION:
                sb.append(" - ");
                break;
            case MULTIPLICATION:
                sb.append(" * ");
                break;
            case DIVISION:
                sb.append(" / ");
                break;
        }


        sb.append(vars[1]);


        if(ops[0] >= MULTIPLICATION && ops[1] >= MULTIPLICATION)
        {
            sb.append(")");
        }


        switch (ops[1])
        {
            case ADDITION:
                sb.append(" + ");
                break;
            case SUBTRACTION:
                sb.append(" - ");
                break;
            case MULTIPLICATION:
                sb.append(" * ");
                break;
            case DIVISION:
                sb.append(" / ");
                break;
        }


        sb.append(vars[2]);

        return sb.toString();
    }


    private int add(int x, int y)
    {
        int answer = x + y;
        return answer;
    }


    private int subtract(int x, int y)
    {
        int answer = x - y;
        return answer;
    }


    private int multiply(int x, int y)
    {
        int answer = x * y;
        return answer;
    }


    private int divide(int x, int y)
    {
        int answer = x / y;
        return answer;
    }


    public int getAns() {
        return ans;
    }

    public int[] getVars()
    {
        return vars;
    }

    public int[] getOps()
    {
        return ops;
    }

    public String getEquation()
    {
        return finalEquation;
    }
}

