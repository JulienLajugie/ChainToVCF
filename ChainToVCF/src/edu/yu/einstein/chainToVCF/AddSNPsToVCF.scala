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

import com.beust.jcommander.Parameter
import com.beust.jcommander.JCommander

object AddSNPsToVCF {

  object Args {
    @Parameter(names = Array("-h", "--help"), help = true, description = "Show usage.")
    var help: Boolean = _

    @Parameter(names = Array("-c", "--chain"), description = "Path to the chain file (eg: ./hg19ToHg38.over.chain).", required = true)
    var chainFilePath: String = _

    @Parameter(names = Array("-b", "--bgr"), description = "Path to the bgr file containing SNPs information (eg: ./hg19ToHg38-SNP.bgr).", required = true)
    var bedFilePath: String = _

    @Parameter(names = Array("-t", "--target"), description = "Name of the target reference genome (eg: hg38).", required = true)
    var targetReference: String = _

    @Parameter(names = Array("-s", "--source"), description = "Name of the source reference genome (eg: hg19).", required = true)
    var sourceReference: String = _
  }

  def main(args: Array[String]): Unit = {
    val params = new JCommander(Args, args.toArray: _*)
    if (Args.help) {
      params.setProgramName("scala -classpath ChainToVCF.jar edu.yu.einstein.chainToVCF.AddSNPsToVCF")
      params.usage
    } else {
      val vcfWriter = new VCFWriter(Args.targetReference, Args.sourceReference)
      val chainReader = new ChainReader(Args.chainFilePath)
      val bedReader = new BgrReader(Args.bedFilePath)
      val variantList = chainReader.retrieveVariants ++ bedReader.retrieveVariants
      vcfWriter.printVariantList(variantList)
    }
  }
}