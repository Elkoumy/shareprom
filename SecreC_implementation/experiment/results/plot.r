#install.packages("sjPlot")
library(sjPlot)
#install.packages("sjmisc")
library(sjmisc)
#install.packages("sjlabelled")
library(sjlabelled)

theme_set(theme_bw())

time <- read.csv(file="time_result_transposed3.csv", header=TRUE, sep=",")
time$chunks=as.factor(time$chunks)



#install.packages("ggplot2")
library(ggplot2)

ggplot(data = time, mapping = aes(x = chunks, y = full_run)) +
  #geom_bar(stat = "identity", aes(y = per_chunk_sort)) +
  geom_bar(stat = "identity",fill=time$per_chunk_sort)+
 # geom_col(aes(y = per_chunk_sort), width = 0.7)+
  #geom_text(aes(y = per_chunk_sort, label = full_run, group =per_chunk_sort), color = "white")+
  facet_wrap(time$dataset)


time$dataset <- factor(time$dataset, levels = c( "credit","bpi", "traffic"), 
                  labels = c("Credit Requirement","BPIC 2017",  "Traffic Fines"))

time$type <- factor(time$type, levels = c( "Combine Results","Parallel Sort"), 
                       labels = c("Combine Results","Parallel Sort"))

ggplot() + geom_bar(aes(y = time, x = chunks, fill = type), data = time,
                    stat="identity", width = 0.4,  geom.colors = "gs", position = position_stack(reverse = TRUE))+
#  ggtitle("Latency Benchmark") +
  ylab("Time (minutes)") + xlab("No. of Chunks")+
  facet_wrap(time$dataset)+ scale_fill_manual("", values = c(  "Combine Results"="#D55E00", "Parallel Sort" = "#56B4E9"))+
theme(panel.grid.major = element_blank(), panel.grid.minor = element_blank(),
      panel.background = element_blank(), strip.background =element_blank(), plot.title = element_text(hjust = 0.5))


communication <- read.csv(file="communiation_result3.csv", header=TRUE, sep=",")
communication$chunks=as.factor(communication$chunks)
communication$server=as.factor(communication$server)

communication$dataset <- factor(communication$dataset, levels = c( "creditReq","bpi", "traffic"), 
                       labels = c( "Credit Requirement","BPIC 2017", "Traffic Fines"))

#communication$sentLabel <- rep("sent",nrow(communication))
#communication$receivedLabel <- rep("received",nrow(communication))

ggplot(data=communication) +
  geom_bar( width = 0.4,stat = "identity" , aes(x=as.numeric(chunks)-0.25, y=sent,group=server,fill=server) )+
#geom_text(position = position_dodge(0.001),aes(x=as.numeric(chunks)-0.25, y=2,group=server,fill=server,label=sentLabel) , angle = 90)+
  geom_bar(width = 0.4,stat = "identity" , aes(x=as.numeric(chunks)+0.25, y=received,group=server,fill=server) )+
 # geom_text(position = position_dodge(0.001),aes(x=as.numeric(chunks)+0.25, y=2,group=server,fill=server,label=receivedLabel) , angle = 90)+
  # ggtitle("Communication Overhead Experiment") +
  scale_y_continuous(trans='log2')+
  ylab("Data Sent (log MB)") + xlab("No. of Chunks")+
 scale_x_continuous(breaks=c(1,2,3,4,5),labels=c("sent receivd\n1","sent receivd\n10","sent receivd\n100","sent receivd\n1000","sent receivd\n10000"))+
  facet_wrap(communication$dataset)+ 
  theme(panel.grid.major = element_blank(), panel.grid.minor = element_blank(),
        panel.background = element_blank(), strip.background =element_blank(), plot.title = element_text(hjust = 0.5))






throughput <- read.csv(file="time_result3.csv", header=TRUE, sep=",")
throughput$chunks=as.factor(throughput$chunks_x)

throughput$dataset <- factor(throughput$dataset, levels = c( "credit", "traffic","bpi"), 
                       labels = c( "Credit Requirement", "Traffic Fines", "BPIC 2017"))

ggplot(data=throughput, aes(x=chunks, y=events_per_second,group=dataset, colour=dataset)) +
  geom_line() +
  geom_point()+
  ylab("Events Per Second") + xlab("No. of Chunks")+
  theme(panel.grid.major = element_blank(), panel.grid.minor = element_blank(),
        panel.background = element_blank(), axis.line = element_line(colour = "black"))
