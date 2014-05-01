package edu.yu.einstein.chainToVCF

class VCFWriter {

  def getLine(variant: Variant): String = {
    variant match {
      case Insertion(chromo, pos, length) =>
        //chromo + "\t" + pos + "\t" + VCFWriter.defaultID +
        "TODO"
      case Deletion(chromo, pos, length) =>
        "TODO"
      case SNP(chromo, pos) =>
        "TODO"
    }
  }
  
}

object VCFWriter {
  val defaultID = "."
  val defautlRef = "N"
  val insertionTag = "<INS>"
  val deletionTag = "<DEL>"
  val defaultQualityField = "1000"
  val defaultFilterField = "PASS"
  val defaultInfoPattern = "IMPRECISE;SVTYPE=%s;END=%d;SVLEN=%d"
  val defaultFormatField = "GT"
  val defaultGTField = "1/1"
  val header = 
    "##fileformat=VCFv4.0\n" +
    "##fileDate=June 08, 2011\n" +
    "##reference=NCBI38-HG38\n" +
    "##ALT=<ID=DEL,Description=\"Deletion\">\n" +
    "##ALT=<ID=INS,Description=\"Insertion of novel sequence\">\n" +
    "##INFO=<ID=IMPRECISE,Number=0,Type=Flag,Description=\"Imprecise structural variation\">\n" +
    "##INFO=<ID=SVTYPE,Number=1,Type=String,Description=\"Type of structural variant\">\n" +
    "##INFO=<ID=END,Number=1,Type=Integer,Description=\"End position of the variant described in this record\">\n" +
    "##INFO=<ID=SVLEN,Number=-1,Type=Integer,Description=\"Difference in length between REF and ALT alleles\">\n" +
    "##FORMAT=<ID=GT,Number=1,Type=String,Description=\"Genotype\">\n";
}