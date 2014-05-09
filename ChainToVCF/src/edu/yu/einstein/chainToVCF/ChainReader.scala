/*******************************************************************************
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
 ******************************************************************************/
package edu.yu.einstein.chainToVCF

import scala.io.Source
import scala.collection.immutable.HashSet

class ChainReader(chainFilePath: String) {

  val chainLines = Source.fromFile(chainFilePath).getLines

  def printChainAsVCF(chaineLine: String, extractedChr: HashSet[String]): HashSet[String] = {
    // extract a chain and print it
    def extractChainContent(chromo: String, position: Int): Unit = {
      if (chainLines.hasNext) {
        val line = chainLines.next
        val splitLine = line.split("\t")
        if (splitLine.length == 3) {
          val varPos = position + Integer.parseInt(splitLine(0))
          val tPos = Integer.parseInt(splitLine(1))
          val qPos = Integer.parseInt(splitLine(2))
          if (tPos > qPos) {
            VCFWriter.printVariant(new Deletion(chromo, varPos, tPos - qPos))
          } else if (tPos < qPos) {
            VCFWriter.printVariant(new Insertion(chromo, varPos, qPos - tPos))
          } else {
            VCFWriter.printVariant(new SNP(chromo, varPos + 1, 'N', 'N'))
          }
          extractChainContent(chromo, varPos + tPos)
        }
      }
    }

    val splitLine = chaineLine.split(" ")
    val tChr = splitLine(ChainReader.refChr)
    if (!extractedChr.contains(tChr)) {
      val qChr = splitLine(ChainReader.queryChr)
      val tStrand = splitLine(ChainReader.refStrand)
      val qStrand = splitLine(ChainReader.queryStrand)
      if (tChr.equals(qChr) && tStrand.equals(qStrand)) {
        val tPos = Integer.parseInt(splitLine(ChainReader.refPos))
        val qPos = Integer.parseInt(splitLine(ChainReader.queryPos))
        if (tPos > qPos) {
          VCFWriter.printVariant(new Deletion(tChr, tPos, tPos - qPos))
        } else if (tPos < qPos) {
          VCFWriter.printVariant(new Insertion(tChr, tPos, qPos - tPos))
        }
        extractChainContent(tChr, tPos)
      }
      extractedChr
    }
    extractedChr + tChr
  }

  def printChainFileAsVCF(): Unit = {
    def chainFileToVCF(extractedChr: HashSet[String]): Unit = {
      if (chainLines.hasNext) {
        val line = chainLines.next
        if (line.trim().startsWith("chain")) {
          chainFileToVCF(printChainAsVCF(line, extractedChr))
        } else {
          chainFileToVCF(extractedChr)
        }
      }
    }
    VCFWriter.printVCFHeader
    chainFileToVCF(new HashSet[String])
  }
}


object ChainReader {

  val refChr        = 2     // chromosome (reference sequence) column in a chain file
  val refStrand     = 4     // strand (reference sequence) column in a chain file
  val refPos        = 5     // alignment start position (reference sequence) column in a chain file
  val queryChr      = 7     // chromosome (query sequence) column in a chain file
  val queryStrand   = 9     // strand (query sequence) column in a chain file
  val queryPos      = 10    // alignment start position (query sequence) column in a chain file

  def main(args: Array[String]): Unit = {
    new ChainReader("/home/jlajugie/Downloads/hg38ToHg19.over.chain").printChainFileAsVCF
  }
}
