package com.example.miteshbhai.pp;

/**
 * Created by miteshbhai on 21-02-2015.
 */
public class Constantvalues {
    public static String[] QuesArray = {
            "What is Distributed System?  How Distributed System project single system image.",
            "Explain Virtual uniprocessor.",
            "Advantages and design issues of Distributed System",
            "List and explain various distributed computing system.",
            "Differentiate monolithic kernel model and micro kernel model.",
            "Explain Client Server Model.",
            "Differentiate Distributed O.S. and network O.S."
    };

    public static String[] getQuestions() {
        return QuesArray;
    }

    public static String[] AnsArray = {
         "\tA distributed system is a software system in which components located on networked computers communicate and coordinate their actions by passing messages. The components interact with each other in order to achieve a common goal.\n" +
                 "\tTo present a single-system image: The distributed system “looks like” a single computer rather than a collection of separate computers.\n" +
                 "\tTo present a single-system image: Hide internal organization, communication details \n" +
                 "\tProvide uniform interface\n" +
                 "\tEasily expandable Adding new computers is hidden from users\n" +
                 "\tContinuous availability\n" +
                 "\tFailures in one component can be covered by other components\n" +
                 "\tSupported by middleware\n",
         "\tAn OS acts as a resource manager or an arbitrator – Manages CPU, I/O devices, memory \n" +
                 "\t OS provides a virtual interface that is easier to use than hardware \n" +
                 "\t Structure of uniprocessor operating systems: – Monolithic (e.g., MS-DOS, early UNIX) \n" +
                 "\t One large kernel that handles everything – Layered design \n" +
                 "\t Functionality is decomposed into N layers \n" +
                 "\t Each layer uses services of layer N-1 and implements new service(s) for layer N+1\n" +
                 "Microkernel architecture:\n" +
                 "\t Small kernel \n" +
                 "\t User-level servers implement additional functionality\n",
         "Advantages:-\n" +
                 "\tPerformance: very often a collection of processors can provide higher performance (and better price/performance ratio) than a centralized computer.\n" +
                 "\t Distribution: many applications involve, by their nature, spatially separated machines (banking, commercial, automotive system). \n" +
                 "\tReliability (fault tolerance): if some of the machines crash, the system can survive.\n" +
                 "\t Incremental growth: as requirements on processing power grow, new machines can be added incrementally. \n" +
                 "\tSharing of data/resources: shared data is essential to many applications (banking, computer supported cooperative work, reservation systems); other resources can be also shared (e.g. expensive printers).\n" +
                 "\t Communication: facilitates human-to-human communication.\n" +
                 "Issues:-\n" +
                 "\tTransparency \n" +
                 "\t Communication \n" +
                 "\t Performance & scalability \n" +
                 "\t Heterogeneity \n" +
                 "\t Openness \n" +
                 "\t Reliability & fault tolerance \n" +
                 "\t Security\n",
         "Distributed Computing system models can be broadly classified into five categories. They are\n" +
                 "        Minicomputer model\n" +
                 "        Workstation model\n" +
                 "        Workstation – server model\n" +
                 "        Processor – pool model\n" +
                 "        Hybrid model\n" +
                 "\n" +
                 "Minicomputer Model\n" +
                 "\tThe minicomputer model  is a simple extension of the centralized time-sharing system. A distributed computing system based on this model consists of a few minicomputers (they may be large supercomputers as well) interconnected by a communication network. Each minicomputer usually has multiple users simultaneously logged on to it. For this, several interactive terminals are connected to each minicomputer. Each user is logged on to one specific minicomputer, with remote access to other minicomputers. The network allows a user to access remote resources that are available on some machine other than the one on to which the user is currently logged. The minicomputer model may be used when resource sharing (such as sharing of information databases of different types, with each type of database located on a different machine) with remote users is desired. The early ARPAnet is an example of a distributed computing system based on the minicomputer model. \n" +
                 "Workstation Model\n" +
                 "\tA distributed computing system based on the workstation model consists of several workstations interconnected by a communication network. An organization may have several workstations located throughout a building or campus, each workstation equipped with its own disk and serving as a single-user computer. It has been often found that in such an environment, at any one time a significant proportion of the workstations are idle (not being used), resulting in the waste of large amounts of CPU time. Therefore, the idea of the workstation model is to interconnect all these workstations by a high-speed LAN so that idle workstations may be used to process jobs of users who are logged onto other workstations and do not have sufficient processing power at their own workstations to get their jobs processed efficiently.  \n" +
                 "Workstation – Server Model\n" +
                 "\tThe workstation model is a network of personal workstations, each with its own disk and a local file system. A workstation with its own local disk is usually called a diskful workstation and a workstation without a local disk is called a diskless workstation. With the proliferation of high-speed networks, diskless workstations have become more popular in network environments than diskful workstations, making the workstation-server model more popular than the workstation model for building distributed computing systems.\n" +
                 "\tA distributed computing system based on the workstation-server model consists of a few minicomputers and several workstations (most of which are diskless, but a few of which may be diskful) interconnected by a communication network.\n" +
                 "\tNote that when diskless workstations are used on a network, the file system to be used by these workstations must be implemented either by a diskful workstation or by a minicomputer equipped with a disk for file storage. One or more of the minicomputers are used for implementing the file system. Other minicomputers may be used for providing other types of services, such as database service and print service. Therefore, each minicomputer is used as a server machine to provide one or more types of services. Therefore in the workstation-server model, in addition to the workstations, there are specialized machines (may be specialized workstations) for running server processes (called servers) for managing and providing access to shared resources. For a number of reasons, such as higher reliability and better scalability, multiple servers are often used for managing the resources of a particular type in a distributed computing system. For example, there may be multiple file servers, each running on a separate minicomputer and cooperating via the network, for managing the files of all the users in the system. Due to this reason, a distinction is often made between the services that are provided to clients and the servers that provide them. That is, a service is an abstract entity that is provided by one or more servers. For example, one or more file servers may be used in a distributed computing system to provide file service to the users.\n" +
                 "\tIn this model, a user logs onto a workstation called his or her home workstation. Normal computation activities required by the user's processes are performed at the user's home workstation, but requests for services provided by special servers (such as a file server or a database server) are sent to a server providing that type of service that performs the user's requested activity and returns the result of request processing to the user's workstation. Therefore, in this model, the user's processes need not migrated to the server machines for getting the work done by those machines.\n" +
                 "Processor – Pool Model\n" +
                 "\tThe processor-pool model is based on the observation that most of the time a user does not need any computing power but once in a while the user may need a very large amount of computing power for a short time (e.g., when recompiling a program consisting of a large number of files after changing a basic shared declaration). Therefore, unlike the workstation-server model in which a processor is allocated to each user, in the processor-pool model the processors are pooled together to be shared by the users as needed. The pool of processors consists of a large number of microcomputers and minicomputers attached to the network. Each processor in the pool has its own memory to load and run a system program or an application program of the distributed computing system \n" +
                 "Hybrid Model\n" +
                 "\tOut of the four models described above, the workstation-server model, is the most widely used model for building distributed computing systems. This is because a large number of computer users only perform simple interactive tasks such as editing jobs, sending electronic mails, and executing small programs. The workstation-server model is ideal for such simple usage. However, in a working environment that has groups of users who often perform jobs needing massive computation, the processor-pool model is more attractive and suitable.\n",
         "o\t Monolithic kernel is much older than Microkernel, the idea was conceived at the end of the 1980's.\n" +
                 "o\t Monolithic kernels are used in Unix and Linux. Microkernels are used in QNX, L4 and HURD. It was initially used in Mach (not Mac OS X) but later converted into a hybrid kernel. Even Minix is not a pure kernel because device drivers are compiled as part of the kernel .\n" +
                 "o\tMonolithic kernels are faster than microkernels. The first microkernel Mach was 50% slower than Monolithic kernel, while later version like L4 were only 2% or 4% slower than the Monolithic kernel .\n" +
                 "o\tMonolithic kernels generally are bulky. A pure monolithic kernel has to be small in size, to fit into the processor's L1 cache (first generation microkernel).\n" +
                 "o\tIn Monolithic kernels, the device drivers reside in the kernel space while in the Microkernel the device drivers reside in the user space.\n" +
                 "o\tSince the device driver resides in the kernel space, it makes monolithic kernel less secure than microkernel, and failure in the driver may lead to crash. Microkernels are more secure than the monolithic kernel, hence used in some military devices.\n" +
                 "o\tMonolithic kernels use signals and sockets to ensure IPC, microkernel approach uses message queues. 1st gen microkernels poorly implemented IPC so were slow on context switches.\n" +
                 "o\tAdding a new feature to a monolithic system means recompiling the whole kernel, whereas with microkernels you can add new features or patches without recompiling.\n",
         "\tThe client–server model of computing is a distributed application structure that partitions tasks or workloads between the providers of a resource or service, called servers, and service requesters, called clients.[1] Often clients and servers communicate over a computer network on separate hardware, but both client and server may reside in the same system. A server host runs one or more server programs which share their resources with clients. A client does not share any of its resources, but requests a server's content or service function. Clients therefore initiate communication sessions with servers which await incoming requests.\n" +
                 "\tExamples of computer applications that use the client–server model are Email, network printing, and the World Wide Web.\n" +
                 "\tThe client–server characteristic describes the relationship of cooperating programs in an application. The server component provides a function or service to one or many clients, which initiate requests for such services.\n" +
                 "\tServers are classified by the services they provide. For instance, a web server serves web pages and a file server serves computer files. A shared resource may be any of the server computer's software and electronic components, from programs and data to processors and storage devices. The sharing of resources of a server constitute a service.\n" +
                 "\tWhether a computer is a client, a server, or both, is determined by the nature of the application that requires the service functions. For example, a single computer can run web server and file server software at the same time to serve different data to clients making different kinds of requests. Client software can also communicate with server software within the same computer.[2] Communication between servers, such as to synchronize data, is sometimes called inter-server or server-to-server communication.\n",
         "User awareness :\n" +
                 "\n" +
                 "NOS: Users are aware of multiplicity of machines.\n" +
                 "DOS: Users are not aware of multiplicity of machines.\n" +
                 "\n" +
                 "Resource Access :\n" +
                 "\n" +
                 "NOS: Access to resources of various machines is done explicitly by remote logging into the appropriate remote machine or transferring data from remote machines to local machines, via the File Transfer Protocol (FTP) mechanism.\n" +
                 "DOS: Access to remote resources similar to access to local resources.\n" +
                 "\n" +
                 "Computation and Data Migration :\n" +
                 "\n" +
                 "NOS: transfer the the data, to and from the remote server and only the server performs the all or most of the computation.\n" +
                 "DOS: transfer the computation, rather than the data, across the system.\n" +
                 "Process Migration :\n" +
                 "\n" +
                 "NOS: execute an entire process, or parts of it, at the remote server.\n" +
                 "DOS: execute an entire process, or parts of it, at different sites.\n" +
                 "\n" +
                 "Data access : \n" +
                 "\n" +
                 "NOS: run process remotely, and needs to transfer all the data to the server for processing.\n" +
                 "DOS: run process remotely, rather than transfer all data locally.\n" +
                 "\n" +
                 "Architecture model :\n" +
                 "\n" +
                 "NOS: Employs a client-server model\n" +
                 "DOS: Employs a master-slave model.\n"
    };
    public static String getAnsArray(int a) {

        return AnsArray[a];
    }
}
