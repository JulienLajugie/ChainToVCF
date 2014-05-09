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

abstract class Variant(chromosome: String, position: Int)
  case class Insertion(chromosome: String, position: Int, length: Int) extends Variant(chromosome: String, position: Int)
  case class Deletion(chromosome: String, position: Int, length: Int) extends Variant(chromosome: String, position: Int)
  case class SNP(chromosome: String, position: Int, ref: Char, alt: Char) extends Variant(chromosome: String, position: Int)
