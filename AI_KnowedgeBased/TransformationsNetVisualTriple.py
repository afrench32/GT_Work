#TODO: is it bad practice to have a global variable here? Can I do a def like in C?
bc_fixed_noise = int(85 * .6)


class TransformationsNetVisualTriple:

    def __init__(self, problemName, figure1, figure2, figure3):

        self.problemName = problemName
        self.figure1 = figure1
        self.figure2 = figure2
        self.figure3 = figure3

        self.blackCountPattern = None
        self.setTheoryPattern  = []

        self.dpr1to2 = None
        self.dpr2to3 = None
        self.ipr1to2 = None
        self.ipr2to3 = None
        self.xor1to2 = None
        self.xor2to3 = None
        self.diff1m2 = None
        self.diff2m1 = None
        self.diff2m3 = None
        self.diff3m2 = None

        self.getBlackCountPattern()
        self.getSetTheoryPattern()

    def getBlackCountPattern(self):

        diff1to2 = self.figure2.blackCount - self.figure1.blackCount
        diff2to3 = self.figure3.blackCount - self.figure2.blackCount

        if diff1to2 in range(-bc_fixed_noise, bc_fixed_noise) and diff2to3 in range(-bc_fixed_noise, bc_fixed_noise):
            self.blackCountPattern = "fixed"

        else:
            self.blackCountPattern = str(diff1to2) + " " + str(diff2to3)

    # TODO constants need to be fine-tuned. Optimize results
    def getSetTheoryPattern(self):

        f1p = self.figure1.blackCount
        f2p = self.figure2.blackCount
        f3p = self.figure3.blackCount

        if f2p != 0:
            self.dpr1to2 = f1p / f2p
        else:
            self.dpr1to2 = float("inf")
        if f3p != 0:
            self.dpr2to3 = f2p / f3p
        else:
            self.dpr2to3 = float("inf")

        common = (self.figure1 & self.figure2) & self.figure3

        union = self.figure1 | self.figure2
        distance = union.distance(self.figure3)
        if abs(f3p - union.blackCount) < 60 and distance < .27 * union.blackCount:
            self.setTheoryPattern.append("1U2")

        intersection = self.figure2 & self.figure3
        self.ipr2to3 = intersection.blackCount / (f2p + f3p)

        intersection = self.figure1 & self.figure2
        self.ipr1to2 = intersection.blackCount / (f1p + f2p)
        distance = intersection.distance(self.figure3)
        if abs(f3p - intersection.blackCount) < 60 and distance < .2 * intersection.blackCount:
            self.setTheoryPattern.append("1^2")

        difference = self.figure2 - self.figure3
        self.diff2m3 = difference.blackCount / (f2p + f3p)

        difference = self.figure1 - self.figure2
        self.diff1m2 = difference.blackCount / (f1p + f2p)
        distance = difference.distance(self.figure3)
        if len(common.figureData) > 0:
            difference = common | difference
        if abs(f3p - difference.blackCount) < 60 and distance < .26 * difference.blackCount:
            self.setTheoryPattern.append("1-2")

        difference = self.figure3 - self.figure2
        self.diff3m2 = difference.blackCount / (f2p + f3p)

        difference = self.figure2 - self.figure1
        self.diff2m1 = difference.blackCount / (f1p + f2p)
        if len(common.figureData) > 0:
            difference = common | difference
        distance = difference.distance(self.figure3)
        if abs(f3p - difference.blackCount) < 90 and distance < .23 * difference.blackCount:
            self.setTheoryPattern.append("2-1")

        xor = self.figure2 ^ self.figure3
        self.xor2to3 = xor.blackCount / (f2p + f3p)

        xor = self.figure1 ^ self.figure2
        self.xor1to2 = xor.blackCount / (f1p + f2p)
        distance = xor.distance(self.figure3)
        if len(common.figureData) > 0:
            xor = common | xor
        if abs(f3p - xor.blackCount) < 90 and distance < .23 * xor.blackCount:
            self.setTheoryPattern.append("1%2")

    def __str__(self):

        return self.problemName + ":" + self.figure1.name + "->" + self.figure2.name + "->" + self.figure3.name