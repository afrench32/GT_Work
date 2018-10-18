"""
Class representing transformations between two images, defined verbally.
"""

class TransformationsNetVerbalPair:

    # default constructor
    def __init__(self, problemName, figure1, figure2):

        # strings representing names for problem, figure 1 and figure 2, respectively
        self.problemName = problemName
        self.figure1name = figure1.name
        self.figure2name = figure2.name

        # RFigure instances
        self.figure1 = figure1
        self.figure2 = figure2

        # dictionaries: {objectName:RObject}
        self.figure1objects = self.figure1.objects
        self.figure2objects = self.figure2.objects

        # essential fields
        self.correspondingObjects = []
        self.removedObjects = []
        self.addedObjects = []
        self.staticChanges = {}
        self.positionalChanges = {}

        # B.S. ad hoc fields for specific basic problems... sorry not sorry
        self.aboveCountChange = 0
        self.leftCountChange = 0
        self.unfillCountChange = 0
        self.overlapChange = 0

        # if the figures have the same number of objects, catalogue corresponding objects,
        # and positional and non-positional changes
        self.determineCorrespondingObjects()
        self.determineRemovals()
        self.determineAdditions()
        # begin b.s.
        self.determineAboveLeftCountChanges()
        self.determineUnfillCountChange()
        self.determineOverlapCountChange()
        # end b.s.
        for pair in self.correspondingObjects:
            self.catalogueStaticChanges(pair)
            if len(self.figure1objects) == 2 and len(self.figure2objects) == 2:
                self.cataloguePositionalChanges(pair)

        #TODO: add field for change in number of objects per figure (image-wide). Optional now but would be cleaner

    # Update self.correspondingObjects list with tuple pairs of corresponding objects between figures
    def determineCorrespondingObjects(self):

        # if there's only one object in both images, those objects correspond
        if len(self.correspondingObjects) == 0 and len(self.figure1objects) == len(self.figure2objects)\
                and len(self.figure1objects) == 1:
            for fig1i in self.figure1objects:
                fig1temp = fig1i
            for fig2i in self.figure2objects:
                fig2temp = fig2i
            self.correspondingObjects.append((fig1temp,fig2temp))
            return

        # determine corresponding objects when there's more than one object per figure
        figure1objects_local = []
        figure2objects_local = []

        for f1object in (self.figure1objects):
            figure1objects_local.append(f1object)
        for f2object in (self.figure2objects):
            figure2objects_local.append(f2object)

        # create scores matrix
        scoresMatrix = {}
        for object1 in figure1objects_local:
            scoresMatrix[object1] = {}
            for object2 in figure2objects_local:
                score = 0
                if self.determineShapeChange(object1, object2) != "no change":
                    score += 7
                else:
                    if self.determineRotation(object1, object2) != 0:
                        score += 2
                    fillChange = self.determineFillChange(object1, object2).split(" to ")
                    if fillChange[0] != fillChange [1]:
                      score += 3
                if len(self.figure1objects) == 2 and len(self.figure2objects) == 2:
                    for attribute in ["left-of", "above"]:
                        if attribute in self.figure1objects[object1].attributes and \
                                attribute in self.figure2objects[object2].attributes:
                            score -= 1
                sizeDiff = self.determineSizeDiff(object1, object2)
                if sizeDiff >= 0:
                    score += sizeDiff
                else:
                    score -= sizeDiff
                scoresMatrix[object1][object2] = score

        # prevents index errors with problems that have figures with no objects
        if len(scoresMatrix) == 0:
            return

        if len(scoresMatrix) < len(scoresMatrix[figure1objects_local[0]]):
            num_matches = len(scoresMatrix)
        else:
            num_matches = len(scoresMatrix[figure1objects_local[0]])

        for match in range(num_matches):
            best = float("inf")
            min_row = None
            min_col = None
            for row in (scoresMatrix):
                for column in (scoresMatrix[row]):
                    if scoresMatrix[row][column] < best:
                        best = scoresMatrix[row][column]
                        min_row = row
                        min_col = column
            self.correspondingObjects.append((min_row, min_col))
            if min_row in scoresMatrix:
                del scoresMatrix[min_row]
            else:
                print("huh? row")
            for row in (scoresMatrix):
                if min_col in scoresMatrix[row]:
                    del scoresMatrix[row][min_col]
                else:
                    print("huh? column")

    # update self.addedObjects list with objects added to figure2
    def determineAdditions(self):

        figure2objects_local = []

        for f2object in self.figure2objects:
            figure2objects_local.append(f2object)

        for object in figure2objects_local:
            hasCorrespondingObject = False
            for pair in self.correspondingObjects:
                if pair[1] == object:
                    hasCorrespondingObject = True
            if not hasCorrespondingObject:
                self.addedObjects.append(object)

    # add (object, None) pairs to correspondingObject if figure1 has n>=1 more figures than figure2
    # also update self.removedObjects list with objects removed from figure1
    def determineRemovals(self):

        figure1objects_local = []

        for f1object in self.figure1objects:
            figure1objects_local.append(f1object)

        for object in figure1objects_local:
            hasCorrespondingObject = False
            for pair in self.correspondingObjects:
                if pair[0] == object:
                    hasCorrespondingObject = True
            if not hasCorrespondingObject:
                self.correspondingObjects.append((object, None))
                self.removedObjects.append(object)

    def determineAboveLeftCountChanges(self):

        f1o = self.figure1objects
        f2o = self.figure2objects

        f1above = 0
        f2above = 0
        f1left = 0
        f2left = 0

        for object in f1o:
            if "above" in f1o[object].attributes:
                f1above += len(f1o[object].attributes["above"])
            if "left-of" in f1o[object].attributes:
                f1left += len(f1o[object].attributes["left-of"])
        for object in f2o:
            if "above" in f2o[object].attributes:
                f2above += len(f2o[object].attributes["above"])
            if "left-of" in f2o[object].attributes:
                f2left += len(f2o[object].attributes["left-of"])

        self.aboveCountChange = f2above - f1above
        self.leftCountChange = f2left - f1left

    def determineUnfillCountChange(self):

        f1unfilled = 0
        f2unfilled = 0

        for object in self.figure1objects:
            if "fill" in self.figure1objects[object].attributes:
                if self.figure1objects[object].attributes["fill"] == "no":
                    f1unfilled += 1
        for object in self.figure2objects:
            if "fill" in self.figure2objects[object].attributes:
                if self.figure2objects[object].attributes["fill"] == "no":
                    f2unfilled += 1

        self.unfillCountChange = f2unfilled - f1unfilled

    def determineOverlapCountChange(self):

        f1overlap = 0
        f2overlap = 0

        for object in self.figure1objects:
            if "overlaps" in self.figure1objects[object].attributes:
                f1overlap += 1
        for object in self.figure2objects:
            if "overlaps" in self.figure2objects[object].attributes:
                f2overlap += 1

        self.overlapChange = f2overlap - f1overlap

    # for a pair of corresponding objects, catalogue the differences between them irrespective of position in figure
    def catalogueStaticChanges(self, pair):

        obj1 = pair[0]
        obj2 = pair[1]

        # if there is no corresponding object in the figure2, mark object in figure1 as "deleted" and return
        if obj2 == None:
            self.staticChanges[obj1] = {"deleted":"deleted"}
            return

        transformations = {}

        sizeDiff = self.determineSizeDiff(obj1, obj2)
        heightDiff = self.determineHeightDiff(obj1, obj2)
        widthDiff = self.determineWidthDiff(obj1, obj2)
        rotation = self.determineRotation(obj1, obj2)
        fillChange = self.determineFillChange(obj1, obj2)
        shapeChange = self.determineShapeChange(obj1, obj2)
        moves = self.determineAlignmentChanges(obj1, obj2)
        if moves is not None:
            verticalMove = moves[0]
            horizontalMove = moves[1]
        reflections = self.determineReflections(obj1, obj2)
        if sizeDiff is not None:
            transformations["sizeDiff"] = sizeDiff
        if heightDiff is not None:
            transformations["heightDiff"] = heightDiff
        if widthDiff is not None:
            transformations["widthDiff"] = widthDiff
        transformations['rotation']= rotation
        transformations['fillChange'] = fillChange
        transformations['shapeChange'] = shapeChange
        if moves is not None:
            transformations['verticalMove'] = verticalMove
            transformations['horizontalMove'] = horizontalMove
        if reflections is not None:
            if len(reflections) > 0:
                transformations['reflections'] = reflections

        self.staticChanges[obj1] = transformations

    # determine size difference. always returns an integer
    def determineSizeDiff(self, obj1, obj2):

        obj1attributes = self.figure1objects[obj1].attributes
        obj2attributes = self.figure2objects[obj2].attributes

        sizes = {"huge": 5, "very large": 4, "large": 3, "medium": 2, "small": 1, "very small": 0}

        if "size" in obj1attributes:
            if obj1attributes['size'] in sizes:
                obj1size = sizes[obj1attributes['size']]
            else:
                print(self, obj1, "size", obj1attributes['size'], "not known")
                obj1size = 2 # default
        else:
            obj1size = 2 # default

        if "size" in obj2attributes:
            if obj2attributes['size'] in sizes:
                obj2size = sizes[obj2attributes['size']]
            else:
                print(self, obj2, "size", obj2attributes['size'], "not known")
                obj2size = 2 # default
        else:
            obj2size = 2 # default

        if obj1size != -1 and obj2size != -1:
            sizeDiff = obj2size - obj1size
        else:
            sizeDiff = None

        return sizeDiff

    # determine height difference. returns an integer or None if height is not given or known
    def determineHeightDiff(self, obj1, obj2):

        obj1attributes = self.figure1objects[obj1].attributes
        obj2attributes = self.figure2objects[obj2].attributes

        heights = {"huge": 5, "very large": 4, "large": 3, "medium": 2, "small": 1, "very small": 0}

        if "height" in obj1attributes or "height" in obj2attributes:
            if "height" in obj1attributes:
                if obj1attributes['height'] in heights:
                    obj1height = heights[obj1attributes['height']]
                else:
                    print(self, obj1, "height", obj1attributes['height'], "not known")
                    obj1height = -1
            else:
                if "size" in obj1attributes:
                    obj1height = heights[obj1attributes["size"]]
                else:
                    # give obj1height whatever value obj2height has
                    obj1height = heights[obj2attributes['height']]  # default

            if "height" in obj2attributes:
                if obj2attributes['height'] in heights:
                    obj2height = heights[obj2attributes['height']]
                else:
                    print(self, obj2, "height", obj2attributes['height'], "not known")
                    obj2height = -1
            else:
                if "size" in obj2attributes:
                    obj2height = heights[obj2attributes["size"]]
                else:
                    # give obj2height whatever value obj1height has
                    obj2height = heights[obj1attributes['height']]  # default

        else:
            obj1height = -1  # default
            obj2height = -1  # default

        if obj1height != -1 and obj2height != -1:
            heightDiff = obj2height - obj1height
        else:
            heightDiff = None

        return heightDiff

    # determine width difference. returns an integer or None if width is not given or known
    def determineWidthDiff(self, obj1, obj2):

        obj1attributes = self.figure1objects[obj1].attributes
        obj2attributes = self.figure2objects[obj2].attributes

        widths = {"huge": 5, "very large": 4, "large": 3, "medium": 2, "small": 1, "very small": 0}

        if "width" in obj1attributes or "height" in obj2attributes:
            if "width" in obj1attributes:
                if obj1attributes['width'] in widths:
                    obj1width = widths[obj1attributes['width']]
                else:
                    print(self, obj1, "width", obj1attributes['width'], "not known")
                    obj1width = -1
            else:
                if "size" in obj1attributes:
                    obj1width = widths[obj1attributes["size"]]
                else:
                    obj1width = widths[obj2attributes['height']]  # default

            if "width" in obj2attributes:
                if obj2attributes['width'] in widths:
                    obj2width = widths[obj2attributes['width']]
                else:
                    print(self, obj2, "width", obj2attributes['width'], "not known")
                    obj2width = -1
            else:
                if "size" in obj2attributes:
                    obj2width = widths[obj2attributes["size"]]
                else:
                    # give obj2height whatever value obj1height has
                    obj2width = widths[obj1attributes['width']]  # default

        else:
            obj1width = -1  # default
            obj2width = -1  # default

        if obj1width != -1 and obj2width != -1:
            widthDiff = obj2width - obj1width
        else:
            widthDiff = None

        return widthDiff

    # determine rotational difference. always returns an integer
    def determineRotation(self, obj1, obj2):

        obj1attributes = self.figure1objects[obj1].attributes
        obj2attributes = self.figure2objects[obj2].attributes

        if "angle" in obj1attributes:
            obj1angle = int(obj1attributes["angle"])
        else:
            obj1angle = 0

        if "angle" in obj2attributes:
            obj2angle = int(obj2attributes["angle"])
        else:
            obj2angle = 0

        rotation = obj2angle - obj1angle
        return rotation

    # determine if the object's fill changes. always returns a string
    def determineFillChange(self, obj1, obj2):

        obj1attributes = self.figure1objects[obj1].attributes
        obj2attributes = self.figure2objects[obj2].attributes

        if "fill" in obj1attributes:
            obj1fill = obj1attributes['fill']
        else:
            print(self, obj2, "has no fill")
            obj1fill = "no fill"

        if "fill" in obj2attributes:
            obj2fill = obj2attributes['fill']
        else:
            print(self, obj2, "has no fill")
            obj2fill = "no fill"

        fillChange = obj1fill + " to " + obj2fill
        return fillChange

    # account for change of shape. always retuns a string
    def determineShapeChange(self, obj1, obj2):

        obj1attributes = self.figure1objects[obj1].attributes
        obj2attributes = self.figure2objects[obj2].attributes

        if "shape" in obj1attributes:
            obj1shape = obj1attributes["shape"]
        else:
            obj1shape = "noShape"
            print(obj1, "has no shape")

        if "shape" in obj2attributes:
            obj2shape = obj2attributes["shape"]
        else:
            obj2shape = "noShape"
            print(obj2, "has no shape")

        if obj1shape == obj2shape:
            shapeChange = "no change"
        else:
            shapeChange = obj1shape + " to " + obj2shape

        return shapeChange

    # account for change in alignment. returns a tuple of two strings if alignment is given, None otherwise
    def determineAlignmentChanges(self, obj1, obj2):

        obj1attributes = self.figure1objects[obj1].attributes
        obj2attributes = self.figure2objects[obj2].attributes

        alignmentCount = 0

        if "alignment" in obj1attributes:
            obj1alignment = obj1attributes['alignment'].split("-")
            alignmentCount += 1
        if "alignment" in obj2attributes:
            obj2alignment = obj2attributes['alignment'].split("-")
            alignmentCount += 1

        verticalMove = None
        horizontalMove = None

        if alignmentCount >= 2:
            if len(obj1alignment) == len(obj2alignment) and len(obj1alignment) == 2:

                if obj1alignment[0] == obj2alignment[0]:
                    verticalMove = "no change"
                elif obj1alignment[0] == "bottom" and obj2alignment[0] == "top":
                    verticalMove = "up"
                elif obj1alignment[0] == "top" and obj2alignment[0] == "bottom":
                    verticalMove = "down"
                else:
                    print("something's up with vertical alignment")

                if obj1alignment[1] == obj2alignment[1]:
                    horizontalMove = "no change"
                elif obj1alignment[1] == "left" and obj2alignment[1] == "right":
                    horizontalMove = "right"
                elif obj1alignment[1] == "right" and obj2alignment[1] == "left":
                    horizontalMove = "left"
                else:
                    print("something's up with horizontal alignment")

            else:
                print("something's up with alignment")

        if verticalMove is not None and horizontalMove is not None:
            moves = (verticalMove, horizontalMove)
            return moves
        else:
            return None

    # catalogues vertical and horizontal movements between a pair of objects
    def cataloguePositionalChanges(self, pair):

        self.determineLateralMovement(pair)
        self.determineVerticalMovement(pair)

    # determines if there is a move left or right between two corresponding objects across figures
    def determineLateralMovement(self, pair):

        # naming convention: figure#object#
        f1o1 = pair[0]
        f2o1 = pair[1]

        if f1o1 is None or f2o1 is None:
            return

        if self.correspondingObjects[0][0] == f1o1:
            f1o2 = self.correspondingObjects[1][0]
            f2o2 = self.correspondingObjects[1][1]
        else:
            f1o2 = self.correspondingObjects[0][0]
            f2o2 = self.correspondingObjects[0][1]

        if f1o2 is None or f2o2 is None:
            return

        f1o1 = self.figure1.objects[f1o1]
        f1o2 = self.figure1.objects[f1o2]
        f2o1 = self.figure2.objects[f2o1]
        f2o2 = self.figure2.objects[f2o2]

        o1initialPos = None
        o2initialPos = None
        o1finalPos = None
        o2finalPos = None

        # if (both o1s are above both o2s) or (if no o1 is above o2 and vice  versa)
        if ("above" in f1o1.attributes and "above" in f2o1.attributes) or \
                ("above" not in f1o1.attributes and "above" not in f1o2.attributes and \
                "above" not in f2o1.attributes and "above" not in f2o2.attributes):

            # determine initial position
            if "left-of" in f1o1.attributes:
                o1initialPos = "left"
                o2initialPos = "right"
            elif "left-of" in f1o2.attributes:
                o1initialPos = "right"
                o2initialPos = "left"
            else:
                o1initialPos = "center"
                o2initialPos = "center"

            # determine final position
            if "left-of" in f2o1.attributes:
                o1finalPos = "left"
                o2finalPos = "right"
            elif "left-of" in f2o2.attributes:
                o1finalPos = "right"
                o2finalPos = "left"
            else:
                o1finalPos = "center"
                o2finalPos = "center"

        # if both o2s are above both o1s, that should be covered when we get to o2 in the list

        if None in [o1initialPos, o2initialPos, o1finalPos, o2finalPos]:

            return

        # if object1 starts left
        if o1initialPos == "left":
            # if object1 ends center or right
            if o1finalPos == "center" or o1finalPos == "right":
                o1move = "right"
            # if object1 ends left
            else:
                o1move = "none"
        # if object1 starts center
        elif o1initialPos == "center":
            # if object1 ends left
            if o1finalPos == "left":
                o1move = "left"
            # if object1 ends center
            elif o1finalPos == "center":
                o1move = "none"
            # if object1 ends right
            else:
                o1move = "right"
        # if object1 starts right
        else:
            # if object1 ends center or left
            if o1finalPos == "center" or o1finalPos == "left":
                o1move = "left"
            # if object1 ends right
            else:
                o1move = "none"

        # TODO: this may be redundant...
        # if object2 starts left
        if o2initialPos == "left":
            # if object2 ends center or right
            if o2finalPos == "center" or o2finalPos == "right":
                o2move = "right"
            # if object2 ends left
            else:
                o2move = "none"
        # if object2 starts center
        elif o2initialPos == "center":
            # if object2 ends left
            if o2finalPos == "left":
                o2move = "left"
            # if object2 ends center
            elif o2finalPos == "center":
                o2move = "none"
            # if object2 ends right
            else:
                o2move = "right"
        # if object2 starts right
        else:
            # if object2 ends center or left
            if o2finalPos == "center" or o2finalPos == "left":
                o2move = "left"
            # if object2 ends right
            else:
                o2move = "none"

        if f1o1.name not in self.positionalChanges:
            self.positionalChanges[f1o1.name] = {"lateralMove":o1move}
        else:
            if "lateralMove" not in self.positionalChanges[f1o1.name]:
                self.positionalChanges[f1o1.name]["lateralMove"] = o1move

        if f1o2.name not in self.positionalChanges:
            self.positionalChanges[f1o2.name] = {"lateralMove": o2move}
        else:
            if "lateralMove" not in self.positionalChanges[f1o2.name]:
                self.positionalChanges[f1o2.name]["lateralMove"] = o2move

    # determines if there is a move up or down between two corresponding objects across figures
    def determineVerticalMovement(self, pair):

        # naming convention: figure#object#
        f1o1 = pair[0]
        f2o1 = pair[1]

        if f1o1 is None or f2o1 is None:
            return

        if self.correspondingObjects[0][0] == f1o1:
            f1o2 = self.correspondingObjects[1][0]
            f2o2 = self.correspondingObjects[1][1]
        else:
            f1o2 = self.correspondingObjects[0][0]
            f2o2 = self.correspondingObjects[0][1]

        if f1o2 is None or f2o2 is None:
            return

        f1o1 = self.figure1.objects[f1o1]
        f1o2 = self.figure1.objects[f1o2]
        f2o1 = self.figure2.objects[f2o1]
        f2o2 = self.figure2.objects[f2o2]

        o1initialPos = None
        o2initialPos = None
        o1finalPos = None
        o2finalPos = None

        # if (both o1s are left-of both o2s) or (if no o1 is left-of o2 and vice  versa)
        if ("left-of" in f1o1.attributes and "left-of" in f2o1.attributes) or \
                ("left-of" not in f1o1.attributes and "left-of" not in f1o2.attributes and \
                "left-of" not in f2o1.attributes and "left-of" not in f2o2.attributes):

            # determine initial position
            if "above" in f1o1.attributes:
                o1initialPos = "above"
                o2initialPos = "below"
            elif "above" in f1o2.attributes:
                o1initialPos = "below"
                o2initialPos = "above"
            else:
                o1initialPos = "center"
                o2initialPos = "center"

            # determine final position
            if "above" in f2o1.attributes:
                o1finalPos = "above"
                o2finalPos = "below"
            elif "above" in f2o2.attributes:
                o1finalPos = "below"
                o2finalPos = "above"
            else:
                o1finalPos = "center"
                o2finalPos = "center"

        # if both o2s are left-of both o1s, that should be covered when we get to o2 in the list

        if None in [o1initialPos, o2initialPos, o1finalPos, o2finalPos]:

            return

        # if object1 starts above
        if o1initialPos == "above":
            # if object1 ends center or below
            if o1finalPos == "center" or o1finalPos == "below":
                o1move = "down"
            # if object1 ends above
            else:
                o1move = "none"
        # if object1 starts center
        elif o1initialPos == "center":
            # if object1 ends above
            if o1finalPos == "above":
                o1move = "up"
            # if object1 ends center
            elif o1finalPos == "center":
                o1move = "none"
            # if object1 ends below
            else:
                o1move = "down"
        # if object1 starts below
        else:
            # if object1 ends center or above
            if o1finalPos == "center" or o1finalPos == "above":
                o1move = "up"
            # if object1 ends below
            else:
                o1move = "none"

        # TODO: this may be redundant...
        # if object2 starts above
        if o2initialPos == "above":
            # if object2 ends center or below
            if o2finalPos == "center" or o2finalPos == "below":
                o2move = "down"
            # if object2 ends above
            else:
                o2move = "none"
        # if object2 starts center
        elif o2initialPos == "center":
            # if object2 ends above
            if o2finalPos == "above":
                o2move = "up"
            # if object2 ends center
            elif o2finalPos == "center":
                o2move = "none"
            # if object2 ends below
            else:
                o2move = "down"
        # if object2 starts below
        else:
            # if object2 ends center or above
            if o2finalPos == "center" or o2finalPos == "above":
                o2move = "up"
            # if object2 ends below
            else:
                o2move = "none"

        if f1o1.name not in self.positionalChanges:
            self.positionalChanges[f1o1.name] = {"verticalMove": o1move}
        else:
            if "verticalMove" not in self.positionalChanges[f1o1.name]:
                self.positionalChanges[f1o1.name]["verticalMove"] = o1move

        if f1o2.name not in self.positionalChanges:
            self.positionalChanges[f1o2.name] = {"verticalMove": o2move}
        else:
            if "verticalMove" not in self.positionalChanges[f1o2.name]:
                self.positionalChanges[f1o2.name]["verticalMove"] = o2move

    #TODO: pentagon, rectangle
    # determine possible reflections. returns a list of strings, or None if shape error
    def determineReflections(self, obj1, obj2):

        # if object2 was deleted, return
        if obj2 == None:
            return

        obj1attributes = self.figure1objects[obj1].attributes
        obj2attributes = self.figure2objects[obj2].attributes

        # if either object does not have a shape attribute, return
        if "shape" not in obj1attributes or "shape" not in obj2attributes:
            return

        # if the objects do not have the same shape, return
        if obj1attributes["shape"] != obj2attributes["shape"]:
            return

        shapes = ["plus", "pac-man", 'square', 'triangle', 'right triangle', 'circle', 'star', 'heart', 'octagon',
                  'pentagon', "diamond", "rectangle"]
        shape = obj1attributes["shape"]

        # if the object's shape is not known, return
        if shape not in shapes:
            print("shape " + shape + " not known")
            return

        # set default rotation of 0 degrees for both objects
        if "angle" not in obj1attributes:
            obj1rotation = 0
        else:
            obj1rotation = int(obj1attributes['angle'])
        if "angle" not in obj2attributes:
            obj2rotation = 0
        else:
            obj2rotation = int(obj2attributes['angle'])

        reflections = []

        #
        # if the shape is a circle or an octagon, all transformations are possible, irrespective of rotation
        #
        if shape == "circle" or shape == "octagon":
            reflections = ["vertical", "horizontal", "diagonal-positive", "diagonal-negative"]

        #
        # plus or square: if the rotations are the same mod 90 (if 0 or 45 reference angle), all rotations possible
        #
        elif shape == "plus" or shape == "square" or shape == "diamond":

            if obj1rotation - obj2rotation % 90 == 0:
                reflections = ["vertical", "horizontal", "diagonal-positive", "diagonal-negative"]

        #
        # triangle, heart, star
        #
        elif shape == "triangle" or shape == "heart" or shape == "star":

            if obj1rotation - obj2rotation in [180, -180]:

                if obj1rotation == 0 or obj2rotation == 0:
                    reflections = ["horizontal"]

                elif obj1rotation == 90 or obj2rotation == 90:
                    reflections = ['vertical']

                elif obj1rotation in [-45, 135] or obj2rotation in [-45, 135]:
                    reflections = ['diagonal-positive']

                elif obj1rotation == 45 or obj2rotation == 45:
                    reflections = ['diagonal-negative']

        #
        # pac-man: horizontal-axis symmetry
        #
        elif shape == "pac-man":

            # if they are in the same position
            if obj1rotation - obj2rotation % 360 == 0:

                if obj1rotation % 180 == 0:
                    reflections = ["horizontal"]

                elif obj1rotation % 90 == 0:
                    reflections = ["vertical"]

            # if they are rotated by 180 degrees
            elif obj1rotation - obj2rotation % 180 == 0:

                if obj1rotation % 180 == 0:
                    reflections = ['vertical']

                elif obj1rotation % 90 == 0:
                    reflections == ['horizontal']

            if obj1rotation in [45, -315]:
                if obj2rotation in [45, -315]:
                    reflections = ['diagonal-negative']
                elif obj2rotation in [135, -225]:
                    reflections = ['vertical']
                elif obj2rotation in [225, -135]:
                    reflections = ['diagonal-positive']
                elif obj2rotation in [315, -45]:
                    reflections = ['horizontal']

            elif obj1rotation in [135, -225]:
                if obj2rotation in [45, -315]:
                    reflections = ['vertical']
                elif obj2rotation in [135, -225]:
                    reflections = ['diagonal-positive']
                elif obj2rotation in [225, -135]:
                    reflections = ['horizontal']
                elif obj2rotation in [315, -45]:
                    reflections = ['diagonal-negative']

            elif obj1rotation in [225, -135]:
                if obj2rotation in [45, -315]:
                    reflections = ['diagonal-positive']
                elif obj2rotation in [135, -225]:
                    reflections = ['horizontal']
                elif obj2rotation in [225, -135]:
                    reflections = ['diagonal-negative']
                elif obj2rotation in [315, -45]:
                    reflections = ['vertical']

            elif obj1rotation in [315, -45]:
                if obj2rotation in [45, -315]:
                    reflections = ['horizontal']
                elif obj2rotation in [135, -225]:
                    reflections = ['diagonal-negative']
                elif obj2rotation in [225, -135]:
                    reflections = ['vertical']
                elif obj2rotation in [315, -45]:
                    reflections = ['diagonal-positive']

        #
        # right triangle
        #
        elif shape == "right triangle":
            if obj1rotation % 90 == 0:

                if obj1rotation in [0, 360]:
                    if obj2rotation in [0, 360]:
                        reflections = ['diagonal-positive']
                    elif obj2rotation in [90, -270]:
                        reflections = ['horizontal']
                    elif obj2rotation in [180, -180]:
                        reflections = ['diagonal-negative']
                    elif obj2rotation in [270, -90]:
                        reflections = ['vertical']

                elif obj1rotation in [90, -270]:
                    if obj2rotation in [0, 360]:
                        reflections = ['horizontal']
                    elif obj2rotation in [90, -270]:
                        reflections = ['diagonal-negative']
                    elif obj2rotation in [180, -180]:
                        reflections = ['vertical']
                    elif obj2rotation in [270, -90]:
                        reflections = ['diagonal-positive']

                elif obj1rotation in [180, -180]:
                    if obj2rotation in [0, 360]:
                        reflections = ['diagonal-negative']
                    elif obj2rotation in [90, -270]:
                        reflections = ['vertical']
                    elif obj2rotation in [180, -180]:
                        reflections = ['diagonal-positive']
                    elif obj2rotation in [270, -90]:
                        reflections = ['horizontal']

                elif obj1rotation in [270, -90]:
                    if obj2rotation in [0, 360]:
                        reflections = ['vertical']
                    elif obj2rotation in [90, -270]:
                        reflections = ['diagonal-positive']
                    elif obj2rotation in [180, -180]:
                        reflections = ['horizontal']
                    elif obj2rotation in [270, -90]:
                        reflections = ['diagonal-negative']

        return reflections

    # represent net as string: problem_name:fiure1->figure2
    def __str__(self):

        return self.problemName + ":" + self.figure1name + "->" + self.figure2name
