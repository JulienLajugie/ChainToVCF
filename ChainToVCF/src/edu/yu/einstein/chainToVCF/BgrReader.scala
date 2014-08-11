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
import scala.collection.immutable.HashMap

class BgrReader(bgrFilePath: String) {

  val chainLines = Source.fromFile(bgrFilePath).getLines

  def retrieveVariants(): VariantList = {
    def retriveVariants(variants: VariantList): VariantList = {
      if (chainLines.hasNext) {
        extractLine(chainLines.next()) match {
          case Some(x) => retriveVariants(variants + x)
          case None => retriveVariants(variants)
        }
      } else {
        variants
      }
    }
    retriveVariants(new VariantList(new HashMap))
  }

  def extractLine(bgrLine: String): Option[SNP] = {
    val splitLine = bgrLine.split("\t")
    if (splitLine.length == 4) {
      val chr = splitLine(0).trim
      val pos = Integer.parseInt(splitLine(1).trim())
      val ref = intToVar(splitLine(3).trim().charAt(0))
      val alt = intToVar(splitLine(3).trim().charAt(1))
      Some(SNP(chr, pos + 1, ref, alt))
    } else {
      None
    }
  }

  def intToVar(intChar: Char): Char = intChar match {
    case '1' => ('A')
    case '2' => ('C')
    case '3' => ('G')
    case '4' => ('T')
  }
}
