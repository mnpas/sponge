"""
Sponge Knowledge base
Demo Forms - Library as action arguments
"""

class ArgLibraryForm(Action):
    def onConfigure(self):
        self.withLabel("Library (books as arguments)")
        self.withArgs([
            StringType("search").withNullable().withLabel("Search").withFeature("responsive", True),
            StringType("order").withLabel("Sort by").withProvided(ProvidedMeta().withValue().withValueSet()),
            # Provided with overwrite to allow GUI refresh.
            ListType("books").withLabel("Books").withProvided(ProvidedMeta().withValue().withOverwrite().withDependencies(
                ["search", "order"])).withFeatures({
                "createAction":SubAction("ArgCreateBook"),
                "readAction":SubAction("ArgReadBook").withArg("bookId", "@this"),
                "updateAction":SubAction("ArgUpdateBook").withArg("bookId", "@this"),
                "deleteAction":SubAction("ArgDeleteBook").withArg("bookId", "@this"),
                "refreshable":True,
            }).withElement(
                IntegerType().withAnnotated()
            )
        ]).withNonCallable().withFeature("icon", "library")
    def onProvideArgs(self, context):
        global LIBRARY
        if "order" in context.provide:
            context.provided["order"] = ProvidedValue().withValue("author").withAnnotatedValueSet([
                AnnotatedValue("author").withValueLabel("Author"), AnnotatedValue("title").withValueLabel("Title")])
        if "books" in context.provide:
            context.provided["books"] = ProvidedValue().withValue(
                map(lambda book: AnnotatedValue(int(book.id)).withValueLabel("{} - {}".format(book.author, book.title)).withValueDescription("Sample description (ID: " + str(book.id) +")"),
                    sorted(LIBRARY.findBooks(context.current["search"]), key = lambda book: book.author.lower() if context.current["order"] == "author" else book.title.lower())))

class ArgCreateBook(Action):
    def onConfigure(self):
        self.withLabel("Add a new book")
        self.withArgs([
            StringType("author").withLabel("Author"),
            StringType("title").withLabel("Title")
        ]).withNoResult()
        self.withFeatures({"visible":False, "callLabel":"Save", "cancelLabel":"Cancel"})

    def onCall(self, author, title):
        global LIBRARY
        LIBRARY.addBook(author, title)

class ArgAbstractReadUpdateBook(Action):
    def onConfigure(self):
        self.withArgs([
            IntegerType("bookId").withAnnotated().withFeature("visible", False),
            StringType("author").withLabel("Author").withProvided(ProvidedMeta().withValue().withDependency("bookId")),
            StringType("title").withLabel("Title").withProvided(ProvidedMeta().withValue().withDependency("bookId"))
        ]).withNoResult()
        self.withFeatures({"visible":False})

    def onProvideArgs(self, context):
        global LIBRARY
        if "author" in context.provide or "title" in context.provide:
            book = LIBRARY.getBook(context.current["bookId"].value)
            context.provided["author"] = ProvidedValue().withValue(book.author)
            context.provided["title"] = ProvidedValue().withValue(book.title)

class ArgReadBook(ArgAbstractReadUpdateBook):
    def onConfigure(self):
        ArgAbstractReadUpdateBook.onConfigure(self)
        self.withLabel("View the book").withNonCallable()
        self.withFeatures({"cancelLabel":"Close"})

    def onCall(self, bookId, author, title):
        pass

class ArgUpdateBook(ArgAbstractReadUpdateBook):
    def onConfigure(self):
        ArgAbstractReadUpdateBook.onConfigure(self)
        self.withLabel("Modify the book")
        self.withFeatures({"callLabel":"Save", "cancelLabel":"Cancel"})

    def onCall(self, bookId, author, title):
        global LIBRARY
        LIBRARY.updateBook(bookId.value, author, title)

class ArgDeleteBook(Action):
    def onConfigure(self):
        self.withLabel("Remove the book")
        self.withArgs([
            IntegerType("bookId").withAnnotated().withFeature("visible", False),
        ]).withNoResult()
        self.withFeatures({"visible":False, "callLabel":"Save", "cancelLabel":"Cancel"})

    def onCall(self, bookId):
        global LIBRARY
        self.logger.info("Deleting book id: {}", bookId.value)
        LIBRARY.removeBook(bookId.value)
