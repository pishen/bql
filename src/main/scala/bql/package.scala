package object bql {
  implicit class BqlHelper(sc: StringContext) {
    def bql(params: Param*) = BQL.create(sc, params: _*)
  }
}
