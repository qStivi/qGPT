# qGPT

## Overview

**qGPT** is an ambitious project aimed at creating an adaptive, interactive chatbot that seamlessly integrates with multiple platforms and interfaces. Built as a modular Java application, **qGPT** is designed to engage users in meaningful conversations across various environments such as in-game chats, websites, and more.

One of the core innovations in qGPT is its **Inner Thoughts Bots**—a multi-bot system that allows the main bot to offload complex tasks to secondary bots, improving efficiency, scalability, and responsiveness.

## Purpose

The primary goal of this project is to develop a conversational AI that serves as a friendly and adaptable chat companion. qGPT is intended to:

- **Engage Users Across Multiple Platforms**: Whether in in-game chats, direct messages, or other interfaces, qGPT interacts with users in a consistent and context-aware manner.
- **Adapt to Context and Users**: Adjust its conversation style based on the current mood, participants, topics, and the communication styles of users to provide a personalized experience.
- **Enhance Interactivity**: Go beyond simple text responses by integrating capabilities like executing in-game commands to enrich user engagement.
- **Be Extensible and Scalable**: Utilize a modular architecture that allows for easy addition of new features and interfaces, making qGPT highly adaptable to future needs.

### **Inner Thoughts Bots**

**Inner Thoughts Bots** are secondary bots that offload complex tasks from the main bot to ensure qGPT remains responsive and efficient. These thought processes allow the main bot to delegate memory searches, complex data processing, or external API requests to the inner bots while continuing its primary tasks.

- **Task Offloading**: The main bot delegates heavy-lifting tasks to inner bots, allowing for parallel processing of memory lookups, deep contextual analysis, or any long-running processes.
- **Concurrency and Scalability**: Multiple inner bots can run concurrently, ensuring qGPT can handle multiple user interactions or tasks simultaneously.
- **Communication and Task Management**: The main bot communicates with inner bots via a task management system, tracking task progress and retrieving results without interrupting the conversation flow.

This architecture allows qGPT to stay focused on user interactions while delegating intensive tasks, optimizing both performance and responsiveness.

## Key Features

- **Modular Architecture**: qGPT is structured with a core engine and separate interface modules (adapters), allowing it to connect to different platforms through well-defined interfaces. This design promotes scalability and ease of maintenance.
- **Inner Thoughts Bots**: Offload complex tasks like memory searches or data processing to separate bots, allowing the main bot to maintain high responsiveness.
- **Memory Management System**:
  - **Global Memory**: Stores public conversation data accessible to all users and interfaces.
  - **User Memory**: Maintains private, user-specific data for personalized interactions, ensuring data privacy and confidentiality.
- **Contextual Formatting**: Responses are formatted according to the interface being used, providing a seamless user experience across platforms.
- **Multi-Threading and Concurrency**: Each interface module operates on its own thread, enabling concurrent interactions without performance degradation. The inner thoughts bots can also run in parallel with the main bot.
- **Extensibility**: The system is designed to allow new interfaces and functionalities to be added with minimal impact on existing components.

## Use Cases

- **In-Game Assistant**: Interacts with players in-game, responds to chat messages, executes commands, and can adopt roles or personas to enhance gameplay.
- **Website Chatbot**: Serves as an interactive assistant on websites, capable of guiding users, answering questions, and providing support.
- **Personal Chat Buddy**: Available for direct messages, offering personalized conversations that adapt over time to the user's preferences and communication style.
- **Task Offloading with Inner Thoughts Bots**: Offload intensive operations like data retrieval or complex calculations, keeping user interactions smooth and uninterrupted.
- **Project Help**: You could aks the bot to keep vast amounts of information about a project, maybe even files. Using the memory search functionality the bot could then assist you more effectively than a traditional chat bot. As they tend to struggle to stay coherent when approaching the token limit. You could for example provide him with the entire lore of you D&D World or Coding project. 

## Project Status

This project is in its early stages and is being developed for fun during free time. It's a personal exploration into the capabilities of conversational AI, modular software design, and multi-bot systems.

## Vision

The project aspires to push the boundaries of conversational AI by creating a bot that is not only functional but also personable and engaging. By focusing on adaptability and user-centric design, qGPT aims to:

- **Provide Meaningful Interactions**: Go beyond scripted responses to offer dynamic and contextually appropriate conversations.
- **Enhance User Experience**: Integrate smoothly into various platforms, adding value whether users are seeking assistance, entertainment, or companionship.
- **Facilitate Easy Integration**: Allow developers to easily add new platforms and features, fostering a community-driven approach to expansion and improvement.

## Contribution

Contributions are welcome! Whether you want to add new features, fix bugs, or improve documentation, your input is valuable.

- **Report Issues**: Use the GitHub issue tracker to report bugs or suggest enhancements.
- **Submit Pull Requests**: Follow the contribution guidelines to submit changes for review.
- **Join the Discussion**: Engage with the community to share ideas and collaborate on improvements.

## License

This project is licensed under the [MIT License](LICENSE), allowing you to use, modify, and distribute the code freely.
