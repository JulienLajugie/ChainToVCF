package edu.yu.einstein.chainToVCF

abstract class Variant(chromosome: String, position: Int)
  case class Insertion(chromosome: String, position: Int, length: Int) extends Variant(chromosome: String, position: Int)
  case class Deletion(chromosome: String, position: Int, length: Int) extends Variant(chromosome: String, position: Int)
  case class SNP(chromosome: String, position: Int, ref: Char, alt: Char) extends Variant(chromosome: String, position: Int)
