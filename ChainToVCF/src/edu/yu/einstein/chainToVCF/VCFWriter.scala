/**
 * *****************************************************************************
 * ChainToVCF
 * Copyright (C) 2014 Albert Einstein College of Medicine
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Authors: Julien Lajugie <julien.lajugie@einstein.yu.edu>
 *
 * Website: <https://github.com/JulienLajugie/ChainToVCF>
 * ****************************************************************************
 */
package edu.yu.einstein.chainToVCF

class VCFWriter(val refName: String, val altName: String) {
  private val columnDelimiter = "\t";
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
      "##reference=" + refName + "\n" +
      "##ALT=<ID=DEL,Description=\"Deletion\">\n" +
      "##ALT=<ID=INS,Description=\"Insertion of novel sequence\">\n" +
      "##INFO=<ID=IMPRECISE,Number=0,Type=Flag,Description=\"Imprecise structural variation\">\n" +
      "##INFO=<ID=SVTYPE,Number=1,Type=String,Description=\"Type of structural variant\">\n" +
      "##INFO=<ID=END,Number=1,Type=Integer,Description=\"End position of the variant described in this record\">\n" +
      "##INFO=<ID=SVLEN,Number=-1,Type=Integer,Description=\"Difference in length between REF and ALT alleles\">\n" +
      "##FORMAT=<ID=GT,Number=1,Type=String,Description=\"Genotype\">\n" +
      "#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT\t" + altName;

  private def getVCFLine(variant: Variant): String = {
    variant match {
      case Insertion(chromo, pos, length) =>
        chromo + columnDelimiter + pos + columnDelimiter + defaultID + columnDelimiter + defautlRef + columnDelimiter +
          insertionTag + columnDelimiter + defaultQualityField + columnDelimiter + defaultFilterField + columnDelimiter +
          defaultSVInfoPattern.format("INS", pos, length) + columnDelimiter + defaultFormatField + columnDelimiter + defaultGTField
      case Deletion(chromo, pos, length) =>
        chromo + columnDelimiter + pos + columnDelimiter + defaultID + columnDelimiter + defautlRef + columnDelimiter +
          deletionTag + columnDelimiter + defaultQualityField + columnDelimiter + defaultFilterField + columnDelimiter +
          defaultSVInfoPattern.format("DEL", pos + length, -length) + columnDelimiter + defaultFormatField + columnDelimiter + defaultGTField
      case SNP(chromo, pos, ref, alt) =>
        chromo + columnDelimiter + pos + columnDelimiter + defaultID + columnDelimiter + ref + columnDelimiter +
          alt + columnDelimiter + defaultQualityField + columnDelimiter + defaultFilterField + columnDelimiter +
          defaultSNPInfoPattern.format(pos) + columnDelimiter + defaultFormatField + columnDelimiter + defaultGTField
    }
  }

  def printVCFHeader(): Unit = {
    println(header)
  }

  def printVariant(variant: Variant): Unit = {
    println(getVCFLine(variant))
  }
}
