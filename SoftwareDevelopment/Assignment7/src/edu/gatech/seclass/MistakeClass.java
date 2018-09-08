package edu.gatech.seclass;

public class MistakeClass {

    public void MistakeClass () {}

    // 100% statement coverage MIGHT NOT reveal fault
    // 100% branch coverage MUST reveal fault
    public void mistakeMethod1 () {

        // This is impossible. The only way to ensure that 100% branch coverage MUST reveal fault is to have a branch
        // dedicated to the divide-by-zero error, which would also require all statements in that branch to be
        // covered in 100% statement coverage, thus revealing the fault in statement coverage as well.

    }

    // <100% statement coverage CAN reveal fault
    // 100% branch coverage MIGHT NOT reveal fault
    public float mistakeMethod2 (float num, float denom) {

        float retval;

        if (num > 2) {
            retval = num / denom;
        }

        else retval = 31;

        return retval;

    }

    // 100% statement coverage MUST reveal fault
    // 100% branch coverage MIGHT NOT reveal fault
    public void mistakeMethod3 () {

        // This is impossible. The only way to ensure that 100% statement coverage MUST reveal the fault is to have a
        // statement that only executes with a zero denominator, which can only be accomplished with a dedicated branch,
        // which would need to be covered in 100% branch coverage as well.

    }

    // all test suites revealing fault have 100% statement coverage
    // <100% branch coverage CAN reveal fault
    public void mistakeMethod4 () {

        // This is impossible. We cannot assume that a test suite has more than one test in it (though any good test
        // suite certainly will). Therefore, since we must have 100% statement coverage to reveal the fault, we cannot
        // have statements that execute without a zero denominator. This means that we cannot have multiple branches,
        // thus meaning there is no way for us to achieve <100% branch coverage.

    }

    public boolean  mistakeMethod5 (boolean a, boolean b) {

        int x = 5;
        int y = 10;

        if (a)
            x = x * 2;
        else
            x = y * x;

        if (b)
            y -= x;
        else
            y += x;

        return ((x / y) > 0);
    }
    // | a | b |output|
    // ================
    // | T | T |  E   |
    // | T | F |  F   |
    // | F | T |  F   |
    // | F | F |  F   |
    // ================
    //
    //Fill in with “never”, “sometimes” or “always”:
    // Statement coverage sometimes reveals the fault in this method.
    // Branch coverage sometimes reveals the fault in this method.
    // Path coverage always reveals the fault in this method.
    // ================

}
