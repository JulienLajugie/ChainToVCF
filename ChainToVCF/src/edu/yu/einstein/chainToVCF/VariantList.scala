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

import java.util.Arrays.ArrayList
import scala.collection.immutable.HashMap

/**
 * List of variants in a Map having chromosome names as keys
 */
class VariantList(val variants: Map[String, List[Variant]]) {

  /**
   * Adds a Variant to the VariantList
   */
  def +(variant: Variant): VariantList = {
    val newVariants = if (variants.contains(variant.chromosome)) variants + (variant.chromosome -> (variant :: variants(variant.chromosome)))
    else variants + (variant.chromosome -> (variant :: Nil))
    new VariantList(newVariants)
  }

  /**
   * Concatains the current VariantList with the specified one
   */
  def ++(variantList: VariantList): VariantList = {
    new VariantList(merge(Seq(variants, variantList.variants)) { (_, v1, v2) => v1 ++ v2 })
  }

  private def merge[K, V](maps: Seq[Map[K, V]])(f: (K, V, V) => V): Map[K, V] = {
    maps.foldLeft(Map.empty[K, V]) {
      case (merged, m) =>
        m.foldLeft(merged) {
          case (acc, (k, v)) =>
            acc.get(k) match {
              case Some(existing) => acc.updated(k, f(k, existing, v))
              case None => acc.updated(k, v)
            }
        }
    }
  }

}