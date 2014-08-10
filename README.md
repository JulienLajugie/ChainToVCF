ChainToVCF
==========

Convert a chain file into a VCF file.

Usage: scala -classpath ChainToVCF.jar edu.yu.einstein.chainToVCF.ChainReader [options]
  Options:
  * -c, --chain
       Path to the chain file (eg: ./hg19ToHg38.over.chain)
  * -h, --help
       Show usage. Default: false
  * -s, --source
       Name of the source reference genome (eg: hg19)
  * -t, --target
       Name of the target reference genome (eg: hg38)
