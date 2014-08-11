ChainToVCF
==========

A tutorial explaining how to use this program can be found at http://genplay.einstein.yu.edu/wiki/index.php/How_to_Create_a_VCF_File_From_a_Chain_File

Convert a chain file into a VCF file.

Usage: scala -classpath ChainToVCF.jar edu.yu.einstein.chainToVCF.ChainToVCF [options]
  Options:
  * -c, --chain
       Path to the chain file (eg: ./hg19ToHg38.over.chain)
  * -h, --help
       Show usage. Default: false
  * -s, --source
       Name of the source reference genome (eg: hg19)
  * -t, --target
       Name of the target reference genome (eg: hg38)


Add SNPs to the VCF file. 

Usage: scala -classpath ChainToVCF.jar edu.yu.einstein.chainToVCF.AddSNPsToVCF [options]
  Options:
  * -b, --bgr
       Path to the bgr file containing SNPs information.
  * -c, --chain
       Path to the chain file (eg: ./hg19ToHg38.over.chain).
    -h, --help
       Show usage.
       Default: false
  * -s, --source
       Name of the source reference genome (eg: hg19).
  * -t, --target
       Name of the target reference genome (eg: hg38).
