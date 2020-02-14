## netty-kcp-ws
       [KCP](https://github.com/skywind3000/kcp)是一个快速可靠协议，能以比 TCP浪费10%-20%的带宽的代价，换取平均延迟降低 
        30%-40%，且最大延迟降低三倍的传输效果。纯算法实现，并不负责底层协议（如UDP）的收发，需要使用者自己定义下层数据包的发送方式，以 callback的方式提供给 KCP。 连时钟都需要外部传递进来，内部不会有任何一次系统调用。
        netty-kcp-ws基于[kcp-netty](https://github.com/szhnet/kcp-netty)
        开发的，类似基于tcp的websocket协议，相比于websocket的优势主要是在于kcp对比tcp的优势，即不需要建立连接和减少延长。