# WORM
1.鉴于豆瓣分类书目，按照评价排序的话，同一类别，超过1000评价的书，不超过400本。
  而面对豆瓣网的反爬虫机制，本程序即使不采取任何应对反爬虫的措施，也可以下载500个网页，
  所以得到的结果是相对准确的。
2.使用方法：首先使用MyCrawlerByThread下载需要的网页，
  然后使用ExtractInfoByThread解析和提取网页信息，
  然后使用txt2excel将结果转化为excel格式。
3.从结果上来看，不尽如人意。预计下载400个页面，实际上只下载了200多个。原因，看看晚上有没有时间找吧。
