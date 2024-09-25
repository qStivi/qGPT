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

*Note: Inner Thoughts Bots are currently in the drafting stage and are planned for future implementation.*

## Key Features

- **Modular Architecture**: qGPT is structured with a core engine and separate interface modules (adapters), allowing it to connect to different platforms through well-defined interfaces. This design promotes scalability and ease of maintenance.
- **Inner Thoughts Bots**: Offload complex tasks like memory searches or data processing to separate bots, allowing the main bot to maintain high responsiveness. *(Planned)*
- **Memory Management System**:
  - **Global Memory**: Stores public conversation data accessible to all users and interfaces. *(Planned)*
  - **User Memory**: Maintains private, user-specific data for personalized interactions, ensuring data privacy and confidentiality. *(Planned)*
- **Contextual Formatting**: Responses are formatted according to the interface being used, providing a seamless user experience across platforms.
- **Extensibility**: The system is designed to allow new interfaces and functionalities to be added with minimal impact on existing components.

## Use Cases

- **In-Game Assistant**: Interacts with players in-game, responds to chat messages, executes commands, and can adopt roles or personas to enhance gameplay.
- **Personal Chat Buddy**: Available for direct messages, offering personalized conversations that adapt over time to the user's preferences and communication style.
- **Task Offloading with Inner Thoughts Bots**: Offload intensive operations like data retrieval or complex calculations, keeping user interactions smooth and uninterrupted. *(Planned)*
- **Project Help**: You could ask the bot to keep vast amounts of information about a project, maybe even files. Using the memory search functionality, the bot could then assist you more effectively than a traditional chatbot, which tends to struggle to stay coherent when approaching the token limit. For example, you could provide it with the entire lore of your D&D world or coding project. *(Planned)*

## Project Status

As of September 2024, **qGPT** is actively evolving with several key milestones achieved:

- **Core Architecture Completed**: The foundational modular architecture has been successfully implemented, allowing for seamless integration of various adapters.
- **Initial Adapters Developed**: Adapters for console interactions have been developed and tested.
- **Comprehensive Testing Suite**: A suite of unit tests ensures the reliability and stability of core components, facilitating ongoing development and maintenance.
- **Documentation Enhanced**: Detailed documentation, including UML diagrams and code comments, provides clarity on system architecture and facilitates contributions.

*Planned Features:*

- **Inner Thoughts Bots**: Designing and implementing secondary bots for task offloading.
- **Memory Management System**: Developing both global and user-specific memory systems.
- **Expanding Platform Integrations**: Developing adapters for additional platforms such as Discord, Slack, and mobile applications.

**qGPT** is progressing steadily towards its vision of becoming a versatile and intelligent chatbot capable of engaging users across a multitude of platforms with personalized and meaningful interactions.

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

This project is licensed under the [MIT License](LICENSE), allowing you to use, modify, and distribute the code under certain conditions.
