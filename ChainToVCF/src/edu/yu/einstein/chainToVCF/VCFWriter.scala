package edu.yu.einstein.chainToVCF

object VCFWriter {
  private val defaultID = "."
  private val defautlRef = "N"
  private val insertionTag = "<INS>"
  private val deletionTag = "<DEL>"
  private val defaultQualityField = "1000"
  private val defaultFilterField = "PASS"
  private val defaultSVInfoPattern = "IMPRECISE;SVTYPE=%s;END=%d;SVLEN=%d"
  private val defaultSNPInfoPattern = "END=%d;SVLEN=1"
  private val defaultFormatField = "GT"
  private val defaultGTField = "1/1"
  private val header = 
    "##fileformat=VCFv4.0\n" +
    "##fileDate=June 08, 2011\n" +
    "##reference=NCBI38-HG38\n" +
    "##ALT=<ID=DEL,Description=\"Deletion\">\n" +
    "##ALT=<ID=INS,Description=\"Insertion of novel sequence\">\n" +
    "##INFO=<ID=IMPRECISE,Number=0,Type=Flag,Description=\"Imprecise structural variation\">\n" +
    "##INFO=<ID=SVTYPE,Number=1,Type=String,Description=\"Type of structural variant\">\n" +
    "##INFO=<ID=END,Number=1,Type=Integer,Description=\"End position of the variant described in this record\">\n" +
    "##INFO=<ID=SVLEN,Number=-1,Type=Integer,Description=\"Difference in length between REF and ALT alleles\">\n" +
    "##FORMAT=<ID=GT,Number=1,Type=String,Description=\"Genotype\">\n" +
    "#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	hg19";
  
  private def getVCFLine(variant: Variant): String = {
    variant match {
      case Insertion(chromo, pos, length) =>
        chromo + "\t" + pos + "\t" + defaultID + "\t" + defautlRef + "\t" + 
        insertionTag + "\t" + defaultQualityField + "\t" + defaultFilterField + "\t" +
        defaultSVInfoPattern.format("INS", pos, length) + "\t" + defaultFormatField + "\t" + defaultGTField
      case Deletion(chromo, pos, length) =>
        chromo + "\t" + pos + "\t" + defaultID + "\t" + defautlRef + "\t" + 
        deletionTag + "\t" + defaultQualityField + "\t" + defaultFilterField + "\t" +
        defaultSVInfoPattern.format("DEL", pos + length, -length) + "\t" + defaultFormatField + "\t" + defaultGTField
      case SNP(chromo, pos, ref, alt) =>
        chromo + "\t" + pos + "\t" + defaultID + "\t" + ref + "\t" + 
        alt + "\t" + defaultQualityField + "\t" + defaultFilterField + "\t" +
        defaultSNPInfoPattern.format(pos) + "\t" + defaultFormatField + "\t" + defaultGTField
    }
  }
  
  
  def printVCFHeader() {
    println(header)
  }
  
  
  def printVariant(variant: Variant) {
    println(getVCFLine(variant))
  }
}