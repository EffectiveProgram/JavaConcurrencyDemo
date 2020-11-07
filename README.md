# JavaConcurrencyDemo
## geekbang笔记
### 1 并发理论基础

### 2 JUC并发工具
- 对于简单的并行任务，你可以通过“线程池 +Future”的方案来解决.
- 如果任务之间有聚合关系，无论是 AND 聚合还是 OR 聚合，都可以通过 CompletableFuture 来解决.
- 批量的并行任务，则可以通过 CompletionService 来解决。
- Fork/Join 并行计算框架主要解决的是分治任务。建议用不同的 ForkJoinPool 执行不同类型的计算任务。