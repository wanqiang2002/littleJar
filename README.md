# littleJar
java小软件
网上关于线程池的八股文太多了我不多说，说了你也记不住，记住了也理解不了，理解了也不会用…

想了很久，终于想出一个demo，加上十个场景，让你能逐步理解线程池真正的工作流程

## 相信我，认真看完这篇文章，你能彻底掌握一个Java核心知识点，不亏 ##

--------------------

线程池无非就那几个参数：核心线程、最大线程、回收时间、队列，没啥难的，有手就能学废

我这里直接上demo，不知道参数啥意思的可以先去隔壁补补课，虽然本文也会提到，但你最好先大概知道点，[线程池实现运行机制总结][Link 1]

**上才艺**

    public class ThreadPoolExecutorTest { 
        private static final int taskCount = 50;//任务数
    
        public static void main(String[] args) throws InterruptedException { 
            AtomicInteger integer = new AtomicInteger();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    10,//核心线程数
                    20,//最大线程数
                    5,//非核心回收超时时间
                    TimeUnit.SECONDS,//超时时间单位
                    new ArrayBlockingQueue<>(30)//任务队列);
            System.out.println("总任务数：" + taskCount);
            long start = System.currentTimeMillis();
            //模拟任务提交
            for (int i = 0; i < taskCount; i++) { 
                Thread thread = new Thread(() -> { 
                    try { 
                        Thread.sleep(500);//模拟执行耗时
                        System.out.println("已执行" + integer.addAndGet(1) + "个任务");
                    } catch (InterruptedException e) { 
                        e.printStackTrace();
                    }
                });
                try { 
                	//注意这里我try起来了，默认拒绝策略会报错
                    executor.execute(thread);
                } catch (Exception e) { 
                    System.out.println(e.getMessage());
                }
            }
            long end = 0;
            while (executor.getCompletedTaskCount() < 50) { 
                end = System.currentTimeMillis();
            }
            System.out.println("任务总耗时：" + (end - start));
        }
    }

**重点来了，我们带着问题来看demo**

如上，new了个线程池，core线程数10，最大线程数20，任务队列容量30，请听题！！

问题0：往上述线程池中提交5个任务，任务执行完总耗时多少？

> 分析：我核心线程数10，也就是说10个线程会长期处于活跃状态，来任务立马能执行，5<10，所以5个任务立马全部执行，多线程并行当然是异步，所以是500ms  
> ![在这里插入图片描述][ac95551b6a7e4f3cac678aec6aef1d8a.png]  
> 插个嘴：为什么不是500而是540，因为代码执行需要花时间，毕竟是模拟提交任务，并不是真正一瞬间提交完

问题1：提交10个任务，总耗时多少？

> 依然是500，10个任务和5个任务其实都一样，没超过核心线程数，来一个执行一个  
> ![][watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_9_color_FFFFFF_t_70_g_se_x_16]

问题2：提交11个任务，总耗时多少？

> 1000，别惊讶，这就是很多人没搞懂线程池机制的关键点，虽然只多了一个任务，但是第11个任务不会马上执行，因为队列没满，所以前10个任务会立马执行，而第11个会被扔到队列中，等有线程空出来了再执行  
> ![][68ba90df46d54a58b100b027b01f6ee6.png]

问题3：提交20个任务，总耗时多少？

> 也是1000，别问为什么也别杠，今天就是耶稣来了它也是1000，这20个任务，前10个任务来一个执行一个，从第11个到第20个会全部丢进队列，当前十个任务有任务执行完了，才会从队列取出执行  
> ![][watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_11_color_FFFFFF_t_70_g_se_x_16]

问题4：提交30个任务，总耗时多少？

> 当然是1500啦，30/10=3，3\*500=1500  
> ![sss](https://img-blog.csdnimg.cn/3035be9aa57e42bda6d870b666e7a892.png)

问题5：提交40个任务，总耗时多少？

> 当然是2000啦，10+10+10+10，我不想解释  
> ![][watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_10_color_FFFFFF_t_70_g_se_x_16]

重点来了！！  
重点来了！！  
重点来了！！

问题6：提交41个任务，总耗时多少？

> 也是2000，很多人会认为是1500，11+11+11+8，其实不然，这里先记下，我后面说，先继续往下看  
> ![][watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_12_color_FFFFFF_t_70_g_se_x_16]

问题7：提交45个任务，总耗时多少？

> 1500，没错就是1500，15+15+15  
> ![][watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_10_color_FFFFFF_t_70_g_se_x_16 1]

问题8：提交50个任务，总耗时多少？

> 1500，20+20+10  
> ![][watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_9_color_FFFFFF_t_70_g_se_x_16 1]  
> 问题9：提交51个任务，总耗时多少？  
> 也是1500，这个线程池同时最大接收50个任务，因为我没设置拒绝策略，默认是AbortPolicy，即超出的任务会被丢弃并抛出RejectedExecutionException异常，demo中没报错是因为我try了

**最后来说刚才的遗留问题，为啥41个任务2000，45个任务就1500??**

其实很多人没把这个搞懂的，后面几个问题我都写了个一串加号（这个搞懂，线程池你就算掌握了）

如40个任务时，10+10+10+10，这代表所有任务分4组完成，每组执行10个，因为多线程是异步，所以**每组执行时间就等于单个任务执行时间**，即500ms，所以40个任务就是500+500+500+500=2000

而41个任务时，是11+11+11+8，所以40个任务也是500+500+500+500=2000（肯定会有小伙伴问，41个任务已经超出了队列容量，线程池中线程为啥没达到最大线程数，应该是20+20+1才对啊）

记住一句话  
任务数 <= 核心线程数时，线程池中工作线程数 = 任务数  
核心线程数 + 队列容量 < 任务数 <= 最大线程数 + 队列容量时，工作线程数 = 任务数 - 队列容量

所以

再来继续看

41个任务时，41-30=11，执行批次为11+11+11+8，即500+500+500+500=2000，有问题吗？没有问题

45个任务时，45-30=15，所以15+15+15，即500+500+500=1500，有问题吗？没有问题

50个任务时，50-30=20，20+20+10，即500+500+500=1500，有问题吗？依然没有问题

--------------------

课后留个问题，44个任务耗时多少？

希望你自己去跑一下，别不识抬举

--------------------

文末补充个冷知识，核心线程数也可以被回收，ThreadPoolExecutor有个属性叫 **allowCoreThreadTimeOut**  
![][ac53d518628547b1b3e37ca36d96ff81.png]  
ThreadPoolExecutor给我们提供了一个public方法allowCoreThreadTimeOut，通过\*\*allowCoreThreadTimeOut( true )\*\*就能设置  
![][3035be9aa57e42bda6d870b666e7a892.png]

--------------------

博客是今天写的

demo是昨天想的

头发是前天想demo时掉的

**这是我好几根头发换来的demo，家里有条件的都把demo复制下来跑一跑，没条件的抄也要抄一遍**

完结  
撒花

--------------------

ok我话说完


[Link 1]: https://blog.csdn.net/qq_33709582/article/details/113614961
[ac95551b6a7e4f3cac678aec6aef1d8a.png]: https://img-blog.csdnimg.cn/ac95551b6a7e4f3cac678aec6aef1d8a.png
[watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_9_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/7b33f218823740b592384bd1a38a51f0.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6LSf5YC656iL5bqP54y_,size_9,color_FFFFFF,t_70,g_se,x_16
[68ba90df46d54a58b100b027b01f6ee6.png]: https://img-blog.csdnimg.cn/68ba90df46d54a58b100b027b01f6ee6.png
[watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_11_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/e85dd22de38d42778fb4cfb526d3498d.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6LSf5YC656iL5bqP54y_,size_11,color_FFFFFF,t_70,g_se,x_16
[watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_13_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/54d2b511f0384fe1a4c889bda4d278fa.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6LSf5YC656iL5bqP54y_,size_13,color_FFFFFF,t_70,g_se,x_16
[watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_10_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/484e59dd1395494995f525ac6e56e0d9.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6LSf5YC656iL5bqP54y_,size_10,color_FFFFFF,t_70,g_se,x_16
[watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_12_color_FFFFFF_t_70_g_se_x_16]: https://img-blog.csdnimg.cn/cebaf769f44c486fabb2683be0dfabb8.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6LSf5YC656iL5bqP54y_,size_12,color_FFFFFF,t_70,g_se,x_16
[watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_10_color_FFFFFF_t_70_g_se_x_16 1]: https://img-blog.csdnimg.cn/95e0e7e46d424b37be2d4df2e72a59d6.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6LSf5YC656iL5bqP54y_,size_10,color_FFFFFF,t_70,g_se,x_16
[watermark_type_ZHJvaWRzYW5zZmFsbGJhY2s_shadow_50_text_Q1NETiBA6LSf5YC656iL5bqP54y_size_9_color_FFFFFF_t_70_g_se_x_16 1]: https://img-blog.csdnimg.cn/c9250986a8414670bba5aa7b50473008.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6LSf5YC656iL5bqP54y_,size_9,color_FFFFFF,t_70,g_se,x_16
[ac53d518628547b1b3e37ca36d96ff81.png]: https://img-blog.csdnimg.cn/ac53d518628547b1b3e37ca36d96ff81.png
[3035be9aa57e42bda6d870b666e7a892.png]: https://img-blog.csdnimg.cn/3035be9aa57e42bda6d870b666e7a892.png