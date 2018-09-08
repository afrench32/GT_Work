from TransformationsNetVerbalPair import TransformationsNetVerbalPair
from RObject import RObject

class TransformationsNetVerbalTriple:

    def __init__(self, problemName, figure1, figure2, figure3):

        self.problemName = problemName
        self.figure1name = figure1.name
        self.figure2name = figure2.name
        self.figure3name = figure3.name

        self.figure1 = figure1
        self.figure2 = figure2
        self.figure3 = figure3

        self.figure1objects = self.figure1.objects
        self.figure2objects = self.figure2.objects
        self.figure3objects = self.figure3.objects

        self.transformations1to2 = TransformationsNetVerbalPair(
            self.problemName, self.figure1, self.figure2)
        self.transformations2to3 = TransformationsNetVerbalPair(
            self.problemName, self.figure2, self.figure3)

        self.correspondingTriples = []
        self.getCorrespondingTriples()

        self.getFigure2PhantomObjects()

        # redo corresponding triples list after getting phantom objects
        self.correspondingTriples = []
        self.getCorrespondingTriples()

        # two types of patterns: meta-patterns like figure count, and object patterns
        # for object properties
        self.metaPatterns = {}
        self.objectPatterns = {}

        self.determineMetaPatterns()
        self.determineObjectPatterns()

    def getCorrespondingTriples(self):

        for pair_1to2 in self.transformations1to2.correspondingObjects:
            for pair_2to3 in self.transformations2to3.correspondingObjects:
                if pair_1to2[1] == pair_2to3[0] and pair_2to3[1] is not None:
                    self.correspondingTriples.append((pair_1to2[0], pair_1to2[1], pair_2to3[1]))

    # if the conditions are right, create an objec in the middle figure to represent one
    # that has been covered up by movement in the other object
    # TODO: not sure if this is overfitting...
    def getFigure2PhantomObjects(self):

        if len(self.figure1objects) != 2 or len(self.figure2objects) != 1 or \
                len(self.figure3objects) != 2 or len(self.correspondingTriples) != 1:
            return

        overlappingObjectFigure1 = self.figure1objects[self.correspondingTriples[0][0]]
        overlappingObjectFigure3 = self.figure3objects[self.correspondingTriples[0][2]]

        phantomObjectFigure1 = None
        phantomObjectFigure3 = None

        for object in self.figure1objects:
            if object != overlappingObjectFigure1.name:
                phantomObjectFigure1 = self.figure1objects[object]
        for object in self.figure3objects:
            if object != overlappingObjectFigure3.name:
                phantomObjectFigure3 = self.figure3objects[object]

        if None in [phantomObjectFigure1, phantomObjectFigure3]:
            return

        sameObject = True
        smaller = phantomObjectFigure1.attributes
        larger = phantomObjectFigure3.attributes
        if len(larger) < len(smaller):
            smaller = phantomObjectFigure3.attributes
            larger = phantomObjectFigure1.attributes
        for attribute in smaller:
            if smaller[attribute] != larger[attribute]:
                sameObject = False

        if sameObject:
            name = phantomObjectFigure1.name + phantomObjectFigure3.name
            phantomObject = RObject(name)
            attributes = {}
            for attribute in phantomObjectFigure1.attributes:
                if attribute != "left-of" and attribute != "above":
                    attributes[attribute] = phantomObjectFigure1.attributes[attribute]
            phantomObject.attributes = phantomObjectFigure1.attributes
            self.figure2.objects[name] = phantomObject
            self.transformations1to2 = TransformationsNetVerbalPair(
                self.problemName, self.figure1, self.figure2)
            self.transformations2to3 = TransformationsNetVerbalPair(
                self.problemName, self.figure2, self.figure3)

    def determineMetaPatterns(self):

        self.determineObjectCountPattern()
        self.determineAboveLeftDeletion()
        self.determineUnfillCountPattern()
        self.determineOverlapCountPattern()

    def determineObjectCountPattern(self):

        diff1to2 = len(self.figure2objects) - len(self.figure1objects)
        diff2to3 = len(self.figure3objects) - len(self.figure2objects)

        if diff1to2 == diff2to3 and diff1to2 == 0:
            self.metaPatterns["objectCount"] = "fixed"

        elif diff1to2 == diff2to3:
            if diff1to2 < 0:
                self.metaPatterns["objectCount"] = "remove " + str(-1 * diff1to2)
            elif diff1to2 > 0:
                self.metaPatterns["objectCount"] = "add " + str(diff1to2)

        # TODO: voodoo
        else:
            self.metaPatterns["objectCount"] = "voodoo " + str(diff1to2) + " " + str(diff2to3)

    def determineAboveLeftDeletion(self):

        t12a = self.transformations1to2.aboveCountChange
        t23a = self.transformations2to3.aboveCountChange
        t12l = self.transformations1to2.leftCountChange
        t23l = self.transformations2to3.leftCountChange

        if (t12a > 0 and t23a > 0) or (t12a < 0 and t23a < 0) or (t12a == 0 and t23a == 0):
            self.metaPatterns["aboveCount"] = "same"
        else:
            self.metaPatterns["aboveCount"] = "split"

        if (t12l > 0 and t23l > 0) or (t12l < 0 and t23l < 0) or (t12l == 0 and t23l == 0):
            self.metaPatterns["leftCount"] = "same"
        else:
            self.metaPatterns["leftCount"] = "split"

    def determineUnfillCountPattern(self):

        diff = self.transformations2to3.unfillCountChange - self.transformations1to2.unfillCountChange

        if diff == 0:
            self.metaPatterns["unfillCount"] = "fixed"
        elif diff > 0:
            self.metaPatterns["unfillCount"] = "add " + str(diff)
        else:
            self.metaPatterns["unfillCount"] = "remove " + str(diff)

    def determineOverlapCountPattern(self):

        oc1to2 = self.transformations1to2.overlapChange
        oc2to3 = self.transformations2to3.overlapChange

        if oc1to2 > 0 and oc2to3 < 0:
            self.metaPatterns["overlapCount"] = "up down"
        elif oc1to2 < 0 and oc2to3 > 0:
            self.metaPatterns["overlapCount"] = "down up"

    def determineObjectPatterns(self):

        for triple in self.correspondingTriples:
            objectPatterns = {}
            if triple[0] is not None and triple[1] is not None and triple[2] is not None:

                # unchanged attributes
                f1a = self.figure1.objects[triple[0]].attributes
                f2a = self.figure2.objects[triple[1]].attributes
                f3a = self.figure3.objects[triple[2]].attributes
                for attribute in f1a:
                    if attribute in f2a and attribute in f3a and attribute != "sizeDiff" and attribute != "heightDiff"\
                            and attribute != "widthDiff":
                        if f1a[attribute] == f2a[attribute] and f2a[attribute] == f3a[attribute]:
                            objectPatterns[attribute] = "unchanged"

                # size, width, height change patterns
                for pattern in ["sizeDiff", "heightDiff", "widthDiff"]:
                    if pattern in self.transformations1to2.staticChanges[triple[0]] and \
                            pattern in self.transformations2to3.staticChanges[triple[1]]:
                        if self.transformations1to2.staticChanges[triple[0]][pattern] > 0 and \
                                self.transformations2to3.staticChanges[triple[1]][pattern] > 0:
                            objectPatterns[pattern] = "increase"
                        elif self.transformations1to2.staticChanges[triple[0]][pattern] < 0 and \
                                self.transformations2to3.staticChanges[triple[1]][pattern] < 0:
                            objectPatterns[pattern] = "decrease"
                        elif self.transformations1to2.staticChanges[triple[0]][pattern] == 0 and \
                                self.transformations2to3.staticChanges[triple[1]][pattern] == 0:
                            objectPatterns[pattern] = "unchanged"

                # movement
                if triple[0] in self.transformations1to2.positionalChanges and \
                        triple[1] in self.transformations2to3.positionalChanges:
                    for move in ["lateralMove", "verticalMove"]:
                        if move in self.transformations1to2.positionalChanges[triple[0]] and \
                                move in self.transformations2to3.positionalChanges[triple[1]]:
                            if self.transformations1to2.positionalChanges[triple[0]][move] == \
                                    self.transformations2to3.positionalChanges[triple[1]][move]:
                                objectPatterns[move] = \
                                    self.transformations1to2.positionalChanges[triple[0]][move]


                # TODO: other patterns

                if len(objectPatterns) > 0:
                    self.objectPatterns[triple[0]] = objectPatterns

    def __str__(self):

        return self.problemName + ":" + self.figure1name + "->" + self.figure2name + "->" + self.figure3name