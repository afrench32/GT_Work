from PIL import Image

size = 100


class RFigureVisual:

    # constructor, works as two different constructors
    def __init__(self, problemName, figure=None, data=None, name=None):

        self.problemName = problemName
        self.figure = figure
        self.data = data
        self.blackCount = 0

        # TODO: exception handling for image reading
        # check out https://pillow.readthedocs.io/en/latest/reference/Image.html#PIL.Image.Image for docs

        if figure is not None:

            self.name = figure.name

            #image = list(Image.open(figure.visualFilename).getdata())
            image = list(Image.open(figure.visualFilename).resize((size, size)).getdata())

            self.figureData = set()
            for i in range(len(image)):
                if image[i] == (0, 0, 0, 255):
                    self.figureData.add(i)
                    self.blackCount += 1

        elif data is not None and name is not None:

            self.figureData = data
            self.name = name
            self.blackCount = len(self.figureData)

        else:

            print(self, "ERROR CREATING RAVENSFIGUREVISUAL INSTANCE")


    # union
    def __or__(self, figure2):

        data = self.figureData | figure2.figureData
        return RFigureVisual(self.problemName, data = data, name =self.name + "+" + figure2.name)

    # intersection
    def __and__(self, figure2):

        data = self.figureData & figure2.figureData
        return RFigureVisual(self.problemName, data = data, name =self.name + "^" + figure2.name)

    # difference
    def __sub__(self, figure2):

        data = self.figureData - figure2.figureData
        return RFigureVisual(self.problemName, data = data, name =self.name + "-" + figure2.name)

    # xor... yeah, I know
    def __xor__(self, figure2):

        data = self.figureData ^ figure2.figureData
        return RFigureVisual(self.problemName, data = data, name =self.name + "-" + figure2.name)

    # method to naively determine distance between two figures: now just number of different pixels
    def distance(self, figure2):

        diff = len(self.figureData ^ figure2.figureData)
        return diff

    # string representation
    def __str__(self):

        return self.problemName + ":" + self.name
