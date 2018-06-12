### TU Delft Master Thesis research project

The thesis' ultimate goal is to allow for Educational Resources (ERs), to be easily findable and re-usable by supporting researchers and ER creators to describe their resources with the appropriate metadata. To do this, we provide **design patterns** or **metadata generation patterns / workflows** covering ways to collect specific types of metadata in the most efficient and easy for the user way. We focus our experimental work on proving the efficiency of the proposed patterns by extracting semantic metadata. This happens by splitting video scrips via Intent Mining algorithms and Text Classification algorithms. 

We call our semantic metadata element **Deductive Intent** and attempt to segment a video's script sentence by sentence into one of the following categories:

- *ConceptDescription*:  Explanation of the Main concept(s) of the LO
- *ConceptMention*:  Concept or other related to the LO term mentioned but not over-viewed in depth right after
- *Summary*:   It refers to a short restatement of the main points of an argument, paper, lecture, etc.
- *Application*:  Practical advise for the concept
- *Example*:   Concept example. Could be of the main or sub-concept

In the end this category will fill in the element mentioned above and this way proving that one of the suggested patterns for Semantic metadata extraction works efficienly.