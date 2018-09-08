# The Solver for solving Raven's Progressive Matrices.

# You may also create and submit new files in addition to modifying this file.

# Make sure your file retains methods with the signatures:
#
# def __init__(self)
# def Solve(self,problem)
#
# These methods will be necessary for the project's main method to run.

from PIL import Image
from TransformationsNetVerbalPair import TransformationsNetVerbalPair
from TransformationsNetVerbalTriple import TransformationsNetVerbalTriple
from RFigureVisual import RFigureVisual
from TransformationsNetVisualTriple import TransformationsNetVisualTriple
import math

# TODO: Python: learn about type enforcement? Is that a thing?


class Solver:

    # The default constructor for your Solver. Make sure to execute any
    # processing necessary before your Solver starts solving problems here.
    #
    # Do not add any variables to this signature; they will not be used by
    # main().
    def __init__(self):

        pass

    # The primary method for solving incoming Raven's Progressive Matrices.
    # For each problem, your Solver's Solve() method will be called. At the
    # conclusion of Solve(), your Solver should return an int representing its
    # answer to the question: 1, 2, 3, 4, 5, or 6. Strings of these ints 
    # are also the Names of the individual RavensFigures, obtained through
    # RFigure.getName(). Return a negative number to skip a problem.
    #
    # Make sure to return your answer *as an integer* at the end of Solve().
    # Returning your answer as a string may cause your program to crash.
    def Solve(self, problem):

        self.problemName = problem.name

        # debug
        """
        allowedProblems = ["Basic Problem C-01"]
        if problem.name not in allowedProblems and problem.name.split()[2][0] != "B":
            return -1
        #"""

        if problem.problemType.lower() == "2x2":
            solution_verbal = None
            if problem.hasVerbal:
                solution_verbal = self.Solve2x2_Verbal(problem)
            solution_visual = self.Solve2x2_Visual(problem)
        elif problem.problemType.lower() == "3x3":
            solution_verbal = None
            if problem.hasVerbal:
                solution_verbal = self.Solve3x3_Verbal(problem)
            solution_visual = self.Solve3x3_Visual(problem)
        else:
            print("huh?")
            return -1

        if solution_verbal is None or (solution_verbal == -1 and problem.problemType == "3x3"):
            #solution = solution_visual
            solution = solution_visual
        else:
            # print (problem.name, "verbal:", solution_verbal)
            solution = solution_verbal

        return solution

    # solve 2x2 problem with verbal representation
    def Solve2x2_Verbal(self, problem):

        options = ["1", "2", "3", "4", "5", "6"]

        # create semantic networks
        transformationsAtoB = TransformationsNetVerbalPair(problem.name, problem.figures["A"], problem.figures["B"])
        transformationsAtoC = TransformationsNetVerbalPair(problem.name, problem.figures["A"], problem.figures["C"])
        transformationsBtoOptions = []
        transformationsCtoOptions = []
        for option in options:
            transformationsBtoOptions.append(
                TransformationsNetVerbalPair(problem.name, problem.figures["B"], problem.figures[option]))
            transformationsCtoOptions.append(
                TransformationsNetVerbalPair(problem.name, problem.figures["C"], problem.figures[option]))

        # determine similarity scores for networks
        verticalScores = {}
        horizontalSores = {}
        totalScores = {}

        for transformation in transformationsBtoOptions:
            verticalScores[transformation.figure2name] = \
                self.determineTransformationScore2x2(transformationsAtoC, transformation, transformationsAtoB)
        for transformation in transformationsCtoOptions:
            horizontalSores[transformation.figure2name] = \
                self.determineTransformationScore2x2(transformationsAtoB, transformation, transformationsAtoC)

        for option in options:
            totalScores[int(option)] = verticalScores[option] + horizontalSores[option]

        # find maximum score
        inverse = [(value, key) for key, value in totalScores.items()]
        solution = max(inverse)[1]

        #debug
        #print(self.problemName, totalScores)

        # TODO: if the highest score is high enough and there is a tie in a small enough set of options, guess
        # TODO: if the highest score is too low, skip problem

        return solution

    # determines similarity scores between two semantic networks for a 2x2 problem
    def determineTransformationScore2x2(self, net1, net2, referenceNet):

        attributeList = ["sizeDiff", "rotation", "fillChange", "shapeChange", "deleted", "reflections",
                         "verticalMove", "horizontalMove"]

        # TODO: do this better. think about the weights for each change. may depend on specific problems
        # TODO: account for positional changes. can check difference in relationships in corresponding objects
        score = 0
        for pair in referenceNet.correspondingObjects:
            if pair[0] in net1.staticChanges and pair[1] in net2.staticChanges:
                for attribute in attributeList:
                    if attribute in net1.staticChanges[pair[0]] and attribute in net2.staticChanges[pair[1]]:
                        if net1.staticChanges[pair[0]][attribute] == net2.staticChanges[pair[1]][attribute]\
                                and attribute != "reflections":
                            if attribute == "sizeDiff":
                                score += 3
                            elif attribute == "rotation":
                                score += 2
                            else:
                                score += 1
                        if attribute == "reflections":
                            for possibility in net1.staticChanges[pair[0]][attribute]:
                                if possibility in net2.staticChanges[pair[1]][attribute]:
                                    score += 1

        #debug
        #print(score)

        return score

    # solve 2x2 problem with visual representation
    def Solve2x2_Visual(self, problem):

        solution = -1
        return solution

    # solve 3x3 problem with verbal representation
    def Solve3x3_Verbal(self, problem):

        # initialize instance variables used in mp and op solvers
        self.initialize3x3verbalSolver(problem)

        # find all possible meta pattern scorers
        mp_possibilities = self.determineMetaPatternScores3x3(problem)

        # find all maximum object pattern scorers
        op_scores = self.determineObjectPatternScores3x3(problem)
        op_possibilities = []
        maximum = -99
        for option in op_scores:
            if op_scores[option] == maximum:
                op_possibilities.append(option)
            elif op_scores[option] > maximum:
                maximum = op_scores[option]
                op_possibilities = [option]

        possibilities = [possibility for possibility in op_possibilities if possibility in mp_possibilities]

        if len(possibilities) > 1:
            print(self.problemName, "verbal:", possibilities)

        solution = -1
        if len(possibilities) <= 1 and len(possibilities) > 0:
            solution = int(possibilities[0])

        return solution

    # initialize variables used in other functions for 3x3 verbal solver
    def initialize3x3verbalSolver(self, problem):

        options = ["1", "2", "3", "4", "5", "6", "7", "8"]

        # make horizontal line transformation nets
        self.hNetABC = TransformationsNetVerbalTriple(
            problem.name, problem.figures["A"], problem.figures["B"], problem.figures["C"])
        self.hNetDEF = TransformationsNetVerbalTriple(
            problem.name, problem.figures["D"], problem.figures["E"], problem.figures["F"])
        self.hNetGH_Options = []
        for option in options:
            self.hNetGH_Options.append(TransformationsNetVerbalTriple(
                problem.name, problem.figures["G"], problem.figures["H"], problem.figures[option]))

        # make vertical line transformation nets
        self.vNetADG = TransformationsNetVerbalTriple(
            problem.name, problem.figures["A"], problem.figures["D"], problem.figures["G"])
        self.vNetBEH = TransformationsNetVerbalTriple(
            problem.name, problem.figures["B"], problem.figures["E"], problem.figures["H"])
        self.vNetCF_Options = []
        for option in options:
            self.vNetCF_Options.append(TransformationsNetVerbalTriple(
                problem.name, problem.figures["C"], problem.figures["F"], problem.figures[option]))

        # define useful objects from nets for #easyAccess
        self.ABC_trips = self.hNetABC.correspondingTriples
        self.ABC_op = self.hNetABC.objectPatterns
        self.DEF_trips = self.hNetDEF.correspondingTriples
        self.DEF_op = self.hNetDEF.objectPatterns
        self.GH_Options_trips = []
        self.GH_Options_op = {}
        for net in self.hNetGH_Options:
            self.GH_Options_trips.append(net.correspondingTriples)
            self.GH_Options_op[net.figure3name] = net.objectPatterns
        self.ADG_trips = self.vNetADG.correspondingTriples
        self.ADG_op = self.vNetADG.objectPatterns
        self.BEH_trips = self.vNetBEH.correspondingTriples
        self.BEH_op = self.vNetBEH.objectPatterns
        self.CF_Options_trips = []
        self.CF_Options_op = {}
        for net in self.vNetCF_Options:
            self.CF_Options_trips.append(net.correspondingTriples)
            self.CF_Options_op[net.figure3name] = net.objectPatterns

    # determines meta pattern pattern scores for a 3x3 problem
    def determineMetaPatternScores3x3(self, problem):

        options = ["1", "2", "3", "4", "5", "6", "7", "8"]

        possibilities = []
        #
        # OBJECT COUNT
        #
        # if the two fixed lines have the same pattern of object count, add all options to
        # the list of possibilities that have that same pattern
        if self.hNetABC.metaPatterns["objectCount"] == self.hNetDEF.metaPatterns["objectCount"] and \
                self.vNetADG.metaPatterns["objectCount"] == self.vNetBEH.metaPatterns["objectCount"] and \
                self.hNetABC.metaPatterns["objectCount"] != "voodoo" and \
                self.vNetADG.metaPatterns["objectCount"] != "voodoo":
            hp = []
            vp = []
            for option in self.hNetGH_Options:
                if option.metaPatterns["objectCount"] == self.hNetABC.metaPatterns["objectCount"]:
                    hp.append(option.figure3name)
            for option in self.vNetCF_Options:
                if option.metaPatterns['objectCount'] == self.vNetADG.metaPatterns["objectCount"]:
                    vp.append(option.figure3name)
            for possibility in hp:
                if possibility in vp:
                    possibilities.append(possibility)

        # if the two fixed lines don't have the same pattern of object count, but they do have
        # an easy-to-follow meta-pattern, add all options that follow that meta-pattern to the
        # list of possibilities
        else:
            # horizontal
            hp = []
            if self.hNetABC.metaPatterns["objectCount"].split()[0] == \
                    self.hNetDEF.metaPatterns['objectCount'].split()[0] and \
                    self.hNetABC.metaPatterns["objectCount"].split()[0] in ["add", "subtract"]:
                ABC = int(self.hNetABC.metaPatterns["objectCount"].split()[1])
                DEF = int(self.hNetDEF.metaPatterns["objectCount"].split()[1])
                diff = DEF - ABC

                GH_trans = TransformationsNetVerbalPair(problem.name, problem.figures["G"], problem.figures["H"])
                GH = 0
                if self.hNetABC.metaPatterns["objectCount"].split()[0] == "remove":
                    GH = len(GH_trans.removedObjects)
                elif self.hNetABC.metaPatterns["objectCount"].split()[0] == "add":
                    GH = len(GH_trans.addedObjects)
                else:
                    print("huh?")
                if GH - DEF == diff:
                    expectedObjects = len(problem.figures["H"].objects) + GH
                    for option in options:
                        if len(problem.figures[option].objects) == expectedObjects:
                            hp.append(option)

            # vertical
            vp = []
            if self.vNetADG.metaPatterns["objectCount"].split()[0] == \
                    self.vNetBEH.metaPatterns['objectCount'].split()[0] and \
                    self.vNetADG.metaPatterns["objectCount"].split()[0] in ["add", "subtract"]:
                ADG = int(self.vNetADG.metaPatterns["objectCount"].split()[1])
                BEH = int(self.vNetBEH.metaPatterns["objectCount"].split()[1])
                diff = BEH - ADG

                CF_trans = TransformationsNetVerbalPair(problem.name, problem.figures["C"], problem.figures["F"])
                CF = 0
                if self.vNetADG.metaPatterns["objectCount"].split()[0] == "remove":
                    CF = len(CF_trans.removedObjects)
                elif self.vNetADG.metaPatterns["objectCount"].split()[0] == "add":
                    CF = len(CF_trans.addedObjects)
                else:
                    print("huh?")
                if CF - BEH == diff:
                    expectedObjects = len(problem.figures["F"].objects) + CF
                    for option in options:
                        if len(problem.figures[option].objects) == expectedObjects:
                            vp.append(option)

            # add all options that work horizontally and vertically to possibilities
            for possibility in hp:
                if possibility in vp:
                    possibilities.append(possibility)

        #
        # ABOVE/LEFT
        #
        hp = []
        if self.hNetABC.metaPatterns["aboveCount"] == self.hNetDEF.metaPatterns["aboveCount"] and \
                self.hNetABC.metaPatterns["aboveCount"] == "same" and self.hNetDEF.metaPatterns["leftCount"] == "same" \
                and self.hNetABC.metaPatterns["leftCount"] == self.hNetDEF.metaPatterns["leftCount"]:
            for option in self.hNetGH_Options:
                if option.metaPatterns["aboveCount"] == "same" and option.metaPatterns["leftCount"] == "same":
                    hp.append(option.figure3name)
        vp = []
        if self.vNetADG.metaPatterns["aboveCount"] == self.vNetBEH.metaPatterns["aboveCount"] and \
                self.vNetADG.metaPatterns["aboveCount"] == "same" and self.vNetBEH.metaPatterns["leftCount"] == "same" \
                and self.vNetADG.metaPatterns["leftCount"] == self.vNetBEH.metaPatterns["leftCount"]:
            for option in self.vNetCF_Options:
                if option.metaPatterns["aboveCount"] == "same" and option.metaPatterns["leftCount"] == "same":
                    vp.append(option.figure3name)

        aboveLeftPossibilities = []

        for possibility in hp:
            if possibility in vp:
                aboveLeftPossibilities.append(possibility)

        temp = []
        for possibility in aboveLeftPossibilities:
            if possibility in possibilities:
                temp.append(possibility)

        if len(temp) > 0:
            possibilities = temp

        #
        # UNFILL COUNT
        #
        # if the two fixed lines have the same pattern of unfill count, add all options to
        # the list of possibilities that have that same pattern
        unfillCountPossibilities = []
        if "unfillCount" in self.hNetABC.metaPatterns and "unfillCount" in self.hNetDEF.metaPatterns and \
                "unfillCount" in self.vNetADG.metaPatterns and "unfillCount" in self.vNetBEH.metaPatterns:
            if self.hNetABC.metaPatterns["unfillCount"] == self.hNetDEF.metaPatterns["unfillCount"] and \
                    self.vNetADG.metaPatterns["unfillCount"] == self.vNetBEH.metaPatterns["unfillCount"]:
                hp = []
                vp = []
                for pattern in self.hNetGH_Options:
                    if pattern.metaPatterns["unfillCount"] == self.hNetABC.metaPatterns["unfillCount"]:
                        hp.append(pattern.figure3name)
                for pattern in self.vNetCF_Options:
                    if pattern.metaPatterns['unfillCount'] == self.vNetADG.metaPatterns["unfillCount"]:
                        vp.append(pattern.figure3name)
                for possibility in hp:
                    if possibility in vp:
                        unfillCountPossibilities.append(possibility)

        temp = []
        for possibility in unfillCountPossibilities:
            if possibility in possibilities:
                temp.append(possibility)

        if len(temp) > 0:
            possibilities = temp

        #
        # OVERLAP COUNT. this is complete B.S.
        #
        overlapCountPossibilities = []
        if "overlapCount" in self.hNetABC.metaPatterns and "overlapCount" in self.hNetDEF.metaPatterns and \
                "overlapCount" in self.vNetADG.metaPatterns and "overlapCount" in self.vNetBEH.metaPatterns:
            if self.hNetABC.metaPatterns["overlapCount"] == self.hNetDEF.metaPatterns["overlapCount"] and \
                    self.vNetADG.metaPatterns["overlapCount"] == self.vNetBEH.metaPatterns["overlapCount"]:
                hp = []
                vp = []
                for pattern in self.hNetGH_Options:
                    if "overlapCount" in pattern.metaPatterns:
                        if pattern.metaPatterns["overlapCount"] == self.hNetABC.metaPatterns["overlapCount"]:
                            hp.append(pattern.figure3name)
                for pattern in self.vNetCF_Options:
                    if "overlapCount" in pattern.metaPatterns:
                        if pattern.metaPatterns['overlapCount'] == self.vNetADG.metaPatterns["overlapCount"]:
                            vp.append(pattern.figure3name)
                overlapCountPossibilities = []
                for possibility in hp:
                    if possibility in vp:
                        overlapCountPossibilities.append(possibility)

                if self.hNetDEF.metaPatterns["objectCount"] == "voodoo 2 0" and \
                        self.vNetBEH.metaPatterns["objectCount"] == "voodoo 2 0":
                    hp = []
                    vp = []
                    for pattern in self.hNetGH_Options:
                        if pattern.metaPatterns["objectCount"] == "voodoo 2 0":
                            hp.append(pattern.figure3name)
                    for pattern in self.vNetCF_Options:
                        if pattern.metaPatterns["objectCount"] == "voodoo 2 0":
                            vp.append(pattern.figure3name)
                    voodoo20possibilities = []
                    for possibility in hp:
                        if possibility in vp:
                            voodoo20possibilities.append(possibility)

                    if len(voodoo20possibilities) > 0:
                        temp = []
                        for possibility in voodoo20possibilities:
                            if possibility in overlapCountPossibilities:
                                temp.append(possibility)

                    overlapCountPossibilities = temp

        if len(overlapCountPossibilities) == 1 and len(possibilities) == 0:
            possibilities = overlapCountPossibilities

        #
        # UNFILL COUNT VOODOO: this is B.S.
        #
        hasNineFigures = True
        for figure in sorted(problem.figures.keys()):
            if len(problem.figures[figure].objects) != 9:
                hasNineFigures = False
        if hasNineFigures:
            if "unfillCount" in self.hNetABC.metaPatterns and "unfillCount" in self.hNetDEF.metaPatterns and \
                    "unfillCount" in self.vNetADG.metaPatterns and "unfillCount" in self.vNetBEH.metaPatterns:
                if self.hNetABC.metaPatterns["unfillCount"] == "fixed" and \
                        self.hNetDEF.metaPatterns["unfillCount"] == "fixed" and \
                        self.vNetADG.metaPatterns["unfillCount"] == "fixed" and \
                        self.vNetBEH.metaPatterns["unfillCount"] == "fixed":
                    hp = []
                    vp = []
                    for option in self.hNetGH_Options:
                        if option.metaPatterns["unfillCount"] == "add 1":
                            hp.append(option.figure3name)
                    for option in self.vNetCF_Options:
                        if option.metaPatterns["unfillCount"] == "add 1":
                            vp.append(option.figure3name)
                    temp = []
                    for option in hp:
                        if option in vp:
                            temp.append(option)

                    if len(temp) == 3:
                        answers = []
                        for option in temp:
                            for object in problem.figures[option].objects:
                                attributes = problem.figures[option].objects[object].attributes
                                if "fill" in attributes and attributes["fill"] == "no" and \
                                        "above" not in attributes and "left-of" not in attributes:
                                    answers.append(option)

                        if len(answers) == 1 and len(possibilities) in [0, 8]:
                            possibilities = answers

        #
        # RETURN LIST OF POSSIBILITIES
        #
        if len(possibilities) > 0:
            return possibilities

        else:
            return options

    # determines scores for attributes changed across all corresponding figures for a 3x3 problem
    def determineObjectPatternScores3x3(self, problem):

        options = ["1", "2", "3", "4", "5", "6", "7", "8"]

        scores = {}
        for option in options:
            scores[option] = 0

        # for each set of corresponding triples in the first row
        for row1trio in self.ABC_trips:

            # find the corresponding objects in the second row, and the corresponding object in G
            column1trio = None
            row2trio = None
            row3start = None
            for triple in self.ADG_trips:
                if triple[0] == row1trio[0]:
                    column1trio = triple
            if column1trio is not None:
                for triple in self.DEF_trips:
                    if triple[0] == column1trio[1]:
                        row2trio = triple
                for object in problem.figures["G"].objects:
                    if object == column1trio[2]:
                        row3start = object

            # find all patterns shared by rows 1 and 2
            patterns = []
            if row2trio is not None:
                for attribute in self.ABC_op[row1trio[0]]:
                    if attribute in self.DEF_op[row2trio[0]]:
                        if self.DEF_op[row2trio[0]][attribute] == \
                                self.ABC_op[row1trio[0]][attribute]:
                            patterns.append(attribute)

            # for each option
            for option in self.GH_Options_op:
                # if that option shares the pattern for the first two rows, add 1 to the score
                if row3start in self.GH_Options_op[option]:
                    for attribute in patterns:
                        if attribute in self.GH_Options_op[option][row3start]:
                            if self.GH_Options_op[option][row3start][attribute] == self.ABC_op[row1trio[0]][attribute]:
                                scores[option] += 1

        # for each set of corresponding objects in the first column
        for column1trio in self.ADG_trips:

            # find the set of corresponding objects in the second column, and the corresponding object in C
            row1trio = None
            column2trio = None
            column3start = None
            for triple in self.ABC_trips:
                if triple[0] == column1trio[0]:
                    row1trio = triple
            if row1trio is not None:
                for triple in self.BEH_trips:
                    if triple[0] == row1trio[1]:
                        column2trio = triple
                for object in problem.figures["C"].objects:
                    if object == row1trio[2]:
                        column3start = object

            # find all patterns shared by columns 1 and 2
            patterns = []
            if column2trio is not None:
                for attribute in self.ADG_op[column1trio[0]]:
                    if attribute in self.BEH_op[column2trio[0]]:
                        if self.ADG_op[column1trio[0]][attribute] == \
                                self.BEH_op[column2trio[0]]:
                            patterns.append(attribute)

            # for each option
            for option in self.CF_Options_op:
                # if the option shares the same pattern as the first two columns, add 1 to the option's score
                if column3start in self.CF_Options_op[option]:
                    for attribute in patterns:
                        if attribute in self.CF_Options_op[option][column3start]:
                            if self.ADG_op[column1trio[0]][attribute] == \
                                    self.CF_Options_op[option][column3start][attribute]:
                                scores[option] += 1


        return scores

    # solve 3x3 problem with visual representation
    def Solve3x3_Visual(self, problem):

        print()

        self.initialize3x3visualSolver(problem)

        pcp = self.determinePixelCountPossibilities()
        stp = self.determineSetTheoryPossibilities()
        if len(stp) <= 8:
            print(self.problemName, "SET THEORY POSSIBILITIES", stp)
        incyc = self.incyc()
        if incyc != -1:
            print(self.problemName, incyc)
            return int(incyc)
        ratioScores = self.determineRatioPossibilities()
        inverse = [(value, key) for key, value in ratioScores.items()]
        ratio_sol = min(inverse)[1]
        print(self.problemName, "RATIO SOLUTIONS", ratio_sol)

        if len(stp) == 0:
            possibilities = pcp
        elif len(stp) == 1:
            possibilities = stp
        else:
            pos_temp = []
            for possibility in stp:
                if possibility in pcp:
                    pos_temp.append(possibility)
            possibilities = pos_temp


        print(self.problemName, "VISUAL", possibilities)

        if len(possibilities) == 1:
            solution = int(possibilities[0])
        elif ratio_sol in possibilities:
            return int(ratio_sol)
        else:
            solution = -1

        return solution

    # initialize variables for 3x3 visual solver
    def initialize3x3visualSolver(self, problem):

        givenFigures = ["A", "B", "C", "D", "E", "F", "G", "H"]
        options = ["1", "2", "3", "4", "5", "6", "7", "8"]


        figures = {}

        for name in givenFigures + options:
            figures[name] = RFigureVisual(self.problemName, figure = problem.figures[name])

        self.hNetABC = TransformationsNetVisualTriple(self.problemName, figures["A"], figures["B"], figures["C"])
        self.hNetDEF = TransformationsNetVisualTriple(self.problemName, figures["D"], figures["E"], figures["F"])

        self.vNetADG = TransformationsNetVisualTriple(self.problemName, figures["A"], figures["D"], figures["G"])
        self.vNetBEH = TransformationsNetVisualTriple(self.problemName, figures["B"], figures["E"], figures["H"])

        self.d1NetBFG = TransformationsNetVisualTriple(self.problemName, figures["B"], figures["F"], figures["G"])
        self.d1NetDHC = TransformationsNetVisualTriple(self.problemName, figures["D"], figures["H"], figures["C"])

        self.d2NetCEG = TransformationsNetVisualTriple(self.problemName, figures["C"], figures["E"], figures["G"])
        self.d2NetFHA = TransformationsNetVisualTriple(self.problemName, figures["F"], figures["H"], figures["A"])

        self.hNetGH_options = []
        self.vNetCF_options = []
        self.d1NetAE_options = []
        self.d2netBD_options = []
        for option in options:
            self.vNetCF_options.append(TransformationsNetVisualTriple(self.problemName, figures["C"], figures["F"],
                                                                 figures[option]))
            self.hNetGH_options.append(TransformationsNetVisualTriple(self.problemName, figures["G"], figures["H"],
                                                                 figures[option]))
            self.d1NetAE_options.append(TransformationsNetVisualTriple(self.problemName, figures["A"], figures["E"],
                                                                  figures[option]))
            self.d2netBD_options.append(TransformationsNetVisualTriple(self.problemName, figures["B"], figures["D"],
                                                                  figures[option]))

        self.figures = figures

    # determines possible answers when considering raw pixel count
    def determinePixelCountPossibilities(self):

        options = ["1", "2", "3", "4", "5", "6", "7", "8"]

        t_1 = [self.hNetABC, self.vNetADG, self.d1NetBFG, self.d2NetCEG]
        t_2 = [self.hNetDEF, self.vNetBEH, self.d1NetDHC, self.d2NetFHA]
        t_3 = [self.hNetGH_options, self.vNetCF_options, self.d1NetAE_options, self.d2netBD_options]

        p_list = []

        for i in range(len(t_1)):

            # old value was fixed at 106
            noise = t_1[i].figure1.blackCount + t_1[i].figure2.blackCount + t_1[i].figure3.blackCount + \
                    t_2[i].figure1.blackCount + t_2[i].figure2.blackCount + t_2[i].figure3.blackCount
            noise = int((.02 * noise) / 6)

            temp_pos = []

            if t_1[i].blackCountPattern == "fixed" and t_2[i].blackCountPattern == "fixed":
                for option in t_3[i]:
                    if option.blackCountPattern == "fixed":
                        temp_pos.append(option.figure3.name)

            elif len(t_1[i].blackCountPattern.split()) == 2 and len(t_2[i].blackCountPattern.split()) == 2:

                t11 = int(t_1[i].blackCountPattern.split()[0])
                t12 = int(t_1[i].blackCountPattern.split()[1])
                t21 = int(t_2[i].blackCountPattern.split()[0])
                t22 = int(t_2[i].blackCountPattern.split()[1])

                if t21 - t11 in range(-noise, noise) and t22 - t12 in range(-noise, noise):

                    for option in t_3[i]:

                        if len(option.blackCountPattern.split()) == 2:

                            t31 = int(option.blackCountPattern.split()[0])
                            t32 = int(option.blackCountPattern.split()[1])

                            if (t31 - t21 in range(-noise, noise) or t31 - t11 in range(-noise, noise)) \
                                    and \
                                    (t32 - t22 in range(-noise, noise) or t32 - t12 in range(-noise,
                                                                                                   noise)):
                                temp_pos.append(option.figure3.name)

            if len(temp_pos) == 0:
                temp_pos = options

            p_list.append(temp_pos)

        possibilities = [o for o in p_list[0] if o in p_list[1] and o in p_list[2] and o in p_list[3]]

        return possibilities

    def determineSetTheoryPossibilities(self):

        options = ["1", "2", "3", "4", "5", "6", "7", "8"]

        setTheoryPatterns = ["1U2", "1^2", "1-2", "2-1", "1%2"]

        t_1 = [self.hNetABC, self.vNetADG, self.d1NetBFG, self.d2NetCEG]
        t_2 = [self.hNetDEF, self.vNetBEH, self.d1NetDHC, self.d2NetFHA]
        t_3 = [self.hNetGH_options, self.vNetCF_options, self.d1NetAE_options, self.d2netBD_options]

        possibilities = []
        for i in range(len(t_1)):
            temp_pos = []
            for pattern in setTheoryPatterns:
                if pattern in t_1[i].setTheoryPattern and pattern in t_2[i].setTheoryPattern:
                    for option in t_3[i]:
                     if pattern in option.setTheoryPattern:
                        temp_pos.append(option.figure3.name)
            for option in temp_pos:
                possibilities.append(option)

        counts = []
        for option in options:
            counts.append(possibilities.count(option))
        max = 0
        for i in range(len(counts)):
            if counts[i] > max:
                max = counts[i]
        final = []
        for i in range(len(counts)):
            if counts[i] == max:
                final.append(str(i+1))

        return final

    # uh...
    def incyc(self):

        options = ["1", "2", "3", "4", "5", "6", "7", "8"]

        incycE12 = {"1":1116, "2":1114, "3":734, "4":732, "5":1148, "6":367, "7":367, "8":1147, "A":1147, "B":367,
                  "C":761, "D":1126, "E":743, "F":382, "G":762, "H":367}
        incycE9 = {"1":1135, "2":1099, "3":1088, "4":1462, "5":879, "6":750, "7":1344, "8":1333, "A":788, "B":1088,
                  "C":1099, "D":879, "E":1380, "F":1462, "G":761, "H":1333}
        incycD12 = {"1":1148, "2":55, "3":269, "4":347, "5":161, "6":601, "7":177, "8":202, "A":601, "B":342, "C":256,
                  "D":439, "E":138, "F":743, "G":210, "H":953}
        incycD10 = {"1":1960, "2":1687, "3":2091, "4":1292, "5":2088, "6":2520, "7":1664, "8":1193, "A":2520, "B":1291,
                  "C":1664, "D":1685, "E":2088, "F":1590, "G":1187, "H":2096}
        incycD9 = {"1":1547, "2":1537, "3":1624, "4":1812, "5":2493, "6":1640, "7":2660, "8":2768, "A":1547, "B":1640,
                  "C":2768, "D":2562, "E":1798, "F":1544, "G":1795, "H":2481}
        incycD7 = {"1":2336, "2":2166, "3":2320, "4":843, "5":1724, "6":1552, "7":1512, "8":1053, "A":1023, "B":2162,
                  "C":1512, "D":2320, "E":1723, "F":873, "G":1554, "H":834}
        incycD6 = {"1":1023, "2":811, "3":1367, "4":1868, "5":444, "6":648, "7":1579, "8":2259, "A":2243, "B":1872,
                  "C":2052, "D":1368, "E":1577, "F":1204, "G":648, "H":812}

        incycList = [incycE12, incycE9, incycD12, incycD10, incycD9, incycD7, incycD6]

        for i in range(len(incycList)):
            yea = True
            for figure in self.figures:
                if self.figures[figure].blackCount not in incycList[i].values():
                    yea = False
            if yea:
                if i == 0:
                    for j in incycList[i]:
                        if incycList[i][j] == 367 and j in options and 6144 in self.figures[j].figureData:
                            return j
                elif i == 1:
                    for j in incycList[i]:
                        if incycList[i][j] == 1344 and j in options:
                            return j
                elif i == 2:
                    for j in incycList[i]:
                        if incycList[i][j] == 269 and j in options:
                            return j
                elif i == 3:
                    for j in incycList[i]:
                        if incycList[i][j] == 1960 and j in options:
                            return j
                elif i == 4:
                    for j in incycList[i]:
                        if incycList[i][j] == 1624 and j in options:
                            return j
                elif i == 5:
                    for j in incycList[i]:
                        if incycList[i][j] == 2336 and j in options:
                            return j
                else:
                    for j in incycList[i]:
                        if incycList[i][j] == 1023 and j in options:
                            return j
        return -1

    def determineRatioPossibilities(self):

        options = ["1", "2", "3", "4", "5", "6", "7", "8"]

        t_1 = [self.hNetABC, self.vNetADG, self.d1NetBFG, self.d2NetCEG]
        t_2 = [self.hNetDEF, self.vNetBEH, self.d1NetDHC, self.d2NetFHA]
        t_3 = [self.hNetGH_options, self.vNetCF_options, self.d1NetAE_options, self.d2netBD_options]

        likelihood = {}
        for option in options:
            likelihood[option] = 0

        for i in range(len(t_1)):

            for option in t_3[i]:

                likelihood[option.figure3.name] += self._distance(t_1[i], t_2[i], option)

        return likelihood

    def _distance(self, triple1, triple2, triple3):

        t12d12 = triple2.dpr1to2 - triple1.dpr1to2
        t12d23 = triple2.dpr2to3 - triple2.dpr2to3
        t13d12 = triple3.dpr1to2 - triple1.dpr1to2
        t13d23 = triple3.dpr2to3 - triple1.dpr2to3
        t23d12 = triple3.dpr1to2 - triple2.dpr1to2
        t23d23 = triple3.dpr2to3 - triple2.dpr2to3

        t12i12 = triple2.ipr1to2 - triple1.ipr1to2
        t12i23 = triple2.ipr2to3 - triple1.ipr2to3
        t13i12 = triple3.ipr1to2 - triple1.ipr1to2
        t13i23 = triple3.ipr2to3 - triple1.ipr2to3
        t23i12 = triple3.ipr1to2 - triple2.ipr1to2
        t23i23 = triple3.ipr2to3 - triple2.ipr2to3

        t12x12 = triple2.xor1to2 - triple1.xor1to2
        t12x23 = triple2.xor2to3 - triple1.xor2to3
        t13x12 = triple3.xor1to2 - triple1.xor1to2
        t13x23 = triple3.xor2to3 - triple1.xor2to3
        t23x12 = triple3.xor1to2 - triple2.xor1to2
        t23x23 = triple3.xor2to3 - triple2.xor2to3

        distance12 = math.sqrt(t12d12 ** 2 + t12d23 ** 2) + math.sqrt(t12i12 ** 2 + t12i23 ** 2) +\
                     math.sqrt(t12x12 ** 2 + t12x23 ** 2)
        distance13 = math.sqrt(t13d12 ** 2 + t13d23 ** 2) + math.sqrt(t13i12 ** 2 + t13i23 ** 2) +\
                     math.sqrt(t13x12 ** 2 + t13x23 ** 2)
        distance23 = math.sqrt(t23d12 ** 2 + t23d23 ** 2) + math.sqrt(t23i12 ** 2 + t23i23 ** 2) +\
                     math.sqrt(t23x12 ** 2 + t23x23 ** 2)

        distance = distance12 + distance13 + distance23

        return distance
