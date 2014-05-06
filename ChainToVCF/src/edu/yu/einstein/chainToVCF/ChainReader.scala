package edu.yu.einstein.chainToVCF

import scala.io.Source
import java.awt.SplashScreen

class ChainReader(chainFilePath: String) {
  
  val chainLines = Source.fromFile(chainFilePath).getLines
  
  def printChainFileAsVCF() {
    def chainFileToVCF() {
      if (chainLines.hasNext) {
        val line = chainLines.next
        if (line.trim().startsWith("chain")) printChainAsVCF(line)
        chainFileToVCF
      }
    }
    
    def printChainAsVCF(chaineLine: String) {

      // extract a chain and print it with 
      def extractChainContent(chromo: String, position: Int) {
        if (chainLines.hasNext) {
          val line = chainLines.next
          val splitLine = line.split("\t")
          if (splitLine.length == 3) {
            val varPos = position + Integer.parseInt(splitLine(0))
            val tPos = Integer.parseInt(splitLine(1))
            val qPos = Integer.parseInt(splitLine(2))
            if (tPos > qPos) VCFWriter.printVariant(new Deletion(chromo, varPos, tPos - qPos))
            else if (tPos < qPos) VCFWriter.printVariant(new Insertion(chromo, varPos, qPos - tPos))
            else VCFWriter.printVariant(new SNP(chromo, varPos + 1, 'N', 'N'))
            extractChainContent(chromo, varPos + tPos)
          }
        }
      }
      
      val splitLine = chaineLine.split(" ")
      val tChr = splitLine(2)
      val qChr = splitLine(7)
      val tStrand = splitLine(4)
      val qStrand = splitLine(9)
      if (tChr.equals(qChr) && tStrand.equals(qStrand)) {
        val tPos = Integer.parseInt(splitLine(5))
        val qPos = Integer.parseInt(splitLine(10))
        if (tPos > qPos) VCFWriter.printVariant(new Deletion(tChr, tPos, tPos - qPos))
        else if (tPos < qPos) VCFWriter.printVariant(new Insertion(tChr, tPos, qPos - tPos))
        extractChainContent(tChr, tPos)
    }
  }
    
    VCFWriter.printVCFHeader
    chainFileToVCF
  } 
}

object ChainReader {
    def main(args: Array[String]): Unit = {
      new ChainReader("/home/jlajugie/Downloads/hg38ToHg19.chr1.over.chain").printChainFileAsVCF
    }
}