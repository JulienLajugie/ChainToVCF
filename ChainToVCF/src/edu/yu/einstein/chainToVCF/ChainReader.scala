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

import scala.io.Source
import scala.collection.immutable.HashSet
import com.beust.jcommander.{ JCommander, Parameter }

class ChainReader(chainFilePath: String, vcfWriter: VCFWriter) {

  val chainLines = Source.fromFile(chainFilePath).getLines

  // print an entire chain file in VCF format
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
    vcfWriter.printVCFHeader
    chainFileToVCF(new HashSet[String])
  }

  // print the entire content of a chain
  def printChainAsVCF(chaineLine: String, extractedChr: HashSet[String]): HashSet[String] = {
    // extract a chain and print it
    val splitLine = chaineLine.split(" ")
    val tChr = splitLine(ChainReader.refChr)
    val qChr = splitLine(ChainReader.queryChr)
    if (!extractedChr.contains(tChr)) {
      val tStrand = splitLine(ChainReader.refStrand)
      val qStrand = splitLine(ChainReader.queryStrand)
      if (tChr.equals(qChr) && tStrand.equals(qStrand)) {
        val tPos = Integer.parseInt(splitLine(ChainReader.refPos))
        val qPos = Integer.parseInt(splitLine(ChainReader.queryPos))
        if (tPos > qPos) {
          vcfWriter.printVariant(new Deletion(tChr, tPos, tPos - qPos))
        } else if (tPos < qPos) {
          vcfWriter.printVariant(new Insertion(tChr, tPos, qPos - tPos))
        }
        extractChainContent(qChr, tPos)
      }
      extractedChr
    }
    extractedChr + qChr
  }

  // extract the content (without the header) of a chain a print it
  def extractChainContent(chromo: String, position: Int): Unit = {
    if (chainLines.hasNext) {
      val line = chainLines.next
      val splitLine = line.split("\t")
      if (splitLine.length == 3) {
        val varPos = position + Integer.parseInt(splitLine(0))
        val tPos = Integer.parseInt(splitLine(ChainReader.bodyRefPos))
        val qPos = Integer.parseInt(splitLine(ChainReader.bodyQueryPos))
        if (tPos != qPos) {
          if (qPos != 0) {
            vcfWriter.printVariant(new Insertion(chromo, varPos, qPos))
          }
          if (tPos != 0) {
            vcfWriter.printVariant(new Deletion(chromo, varPos, tPos))
          }
        } else {
          vcfWriter.printVariant(new SNP(chromo, varPos + 1, 'N', 'N'))
        }
        extractChainContent(chromo, varPos + tPos)
      }
    }
  }
}

object ChainReader {

  object Args {
    @Parameter(names = Array("-h", "--help"), help = true)
    var help: Boolean = _

    @Parameter(names = Array("-c", "--chain"), description = "Path to the chain file (eg: /home/jlajugie/hg19ToHg38.over.chain)", required = true)
    var chainFilePath: String = _

    @Parameter(names = Array("-t", "--target"), description = "Name of the target reference genome (eg: hg38)", required = true)
    var targetReference: String = _

    @Parameter(names = Array("-s", "--source"), description = "Name of the source reference genome (eg: hg19)", required = true)
    var sourceReference: String = _
  }

  val refChr = 7 // chromosome (reference sequence) column in a chain file
  val refStrand = 9 // strand (reference sequence) column in a chain file
  val refPos = 10 // alignment start position (reference sequence) column in a chain file
  val refEnd = 11 // alignment stop position (reference sequence) column in a chain file
  val queryChr = 2 // chromosome (query sequence) column in a chain file
  val queryStrand = 4 // strand (query sequence) column in a chain file
  val queryPos = 5 // alignment start position (query sequence) column in a chain file
  val bodyRefPos = 2 // the difference between the end of this block and the beginning of the next block (reference sequence)
  val bodyQueryPos = 1 // the difference between the end of this block and the beginning of the next block (query sequence)

  def main(args: Array[String]): Unit = {
    val params = new JCommander(Args, args.toArray: _*)
    if (Args.help) {
      params.setProgramName("scala -classpath ChainToVCF.jar edu.yu.einstein.chainToVCF.ChainReader")
      params.usage
    } else {
      val vcfWriter = new VCFWriter(Args.targetReference, Args.sourceReference)
      val chainReader = new ChainReader(Args.chainFilePath, vcfWriter)
      chainReader.printChainFileAsVCF
    }
  }
}
