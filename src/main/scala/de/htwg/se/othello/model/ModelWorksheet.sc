case class Cell(value: Int) {
  def isSet: Boolean = value != 0
  if (value < 0 || value > 2) {
    throw new IllegalArgumentException("Wrong value. Valid values are 0, one and 2")
  }

}

val cell1 = Cell(2)
cell1.isSet

val cell2 = Cell(0)
cell2.isSet

case class Field(cells: Array[Cell])

val field1 = Field(Array.ofDim[Cell](1))

field1.cells(0)=cell1

field1.cells(0).value

val x = 15
