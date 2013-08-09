# Description

**PdfMate** is a small tool that provides several utilities to deal with PDF files. For the time being, its main function is to add the missing *TOC* (Table Of Content) into the PDF files.

# Usage

## Insert TOC

When reading a PDF with long length, it is diffcult to navigate without a **TOC** that links to each part of the content. **PdfMate** solves this problem by providing two operators `extract-pages` and `insert-toc`. The first one can be used to extract the texts on the table of contents pages as
```
$ pdfmate extract-pages <path to PDF>
[Notice]: pdfmate: Input the page range or list:
> 1-3
```
It will ask for the page ranges and print out the extracted text onto screen. The output (in standard error output)  can also be redirected to a file for later editing as
```
$ pdfmate extract-pages <path to PDF> 2> <path to outputted toc text file>
```
By now, you got a text file that contains the contents table and also some other stuffs, so you need manually edit it to the following list of TOC entries:
```
# <hierarchy level> <title> <page number>
1.1 Some Title Words 45
...
```
Any line starting with `#` will be commented. Each line will be an entry in the final TOC with the first two items as the title and the last one as the page number that this entry links to.

Then invoke operator `insert-toc` as
```
$ pdfmate insert-toc <path to PDF> --toc <path to outputted toc text file>
```
A new PDF will be generated with TOC alongside with the original PDF.

**TODO**: Currently, you need to edit the toc text file manually for all the type of PDFs. In future, **PdfMate** should be smart enough to do the dirty job when it is possible for you!

# Author
- Li Dong <dongli@lasg.iap.ac.cn>