### Project structure.

Project contains three instance files:

**_Commit.java_** contains info about commit: 
 - the month when it was done
 
 - developer login
 
 And the info about changed files: 
 
 - filenames count 
 
 Filename here is a filename before first dot so the filenames for files abc.de.js and abc.js are both abc. 
 That is done to calculate files with "similar meaning". That could be improved by using bag of words 
 (e.g. ContributorsGraph and GraphPrinter have one common word) or by using edit distance with some threshold 
 
 - extensions count
 
 People changing .js files and .cpp files are quite different people so this is an important feature.
 Extension here is a an extension after last dot so abc.de.js and abc.js both have js extension.
 
 - packages count
 
 This is important in case of one project 
 (people changing files in same directories could be counted as similar people) but it could be tricky to use it for multi-project graph.
 In that case we could use project names here and calculate similarities between projects.
 
 **_Developer.java_** contains info about contributor and its commits
 
 - Personal info: login, name, id in Github system, and id in our list of top contributors (our id)
 
 - The number of commits person have done grouped by months : that is an info that could be used for vertex description
 
 - The number of filenames, extensions and packages this person have changed grouped by month:
 this is an info that could be used in vertexes and also to calculate distance in edges
 
 **_Edge.java_** contains info about "similarity" between developers
 
 - Info about two developers (ids in Github and in our list)
 
 - Info about connections:
   - Number of filenames that both of developers have changed grouped by month
   - Number of folders that both of developers have changed grouped by month
   - Number of files with same extension that both developers have changed grouped by month and each extension 
   (on the contrary to filenames and folders that's not so much extensions and that's quite different and could have relations one with each other)
 
 Also, to put it all together:
 
 **_GithubParser.java_**
 
 Reads Github API and parses it, creating Commit and Developer classes. Please note that we need Commit just to calculate statistics for Developer
 
 If you'd like to use this file by your own please do not forget to put your Github token there.
 
 **_ContributorsGraph.java_** 
 
 Takes the list of contributors (List<Developer>) with statistics and calculates edges between them.
 
 Contains:
 
 - List of contributors
 - List of existing edges (not-oriented)
 - Adjacency matrix (for ease of getting developer parameters, vertex ids are positions of developers in our list)
 
 **_GraphPrinter.java_** 
 
 Saves graph in a format that could be used e.g. in Python.
 
 - Saves short form of developer list (just vertex properties not corresponding to edge properties). You could find this file at saved_graph/dev_short.txt
 - All properties for developers list: saved in saved_graph/dev_full.txt
 - Edges just like a list: saved in saved_graph/edges.txt
 - Edges in a form of adjacency matrix: saved_graph/adj_matrix.txt
 
 ### Additional comments about solution
 
 -  All calculations are made in terms of grouping by one month period. That seams to be reasonable but this is a parameter that could be changed
 
 - I didn't use distance or just_one_weight for edges: I just have gathered some properties that could be important in this case.
 Actually, it seems like people changing same files are more close (but that's not so much people), so filename should be a strong factor.
 But the number of common folders and extensions are also important and more lightweight (not so much different properties)
 Anyways, it seems to me that it's more convenient to use it in neural networks (and to visualize just by making 2-dimensional latent space)
 
 
 ### What would I do next
 
 - I would try to make 2-d embeddings by PyTorch nets: 
    - make one-hot encoding for developers 
    - by number of commits by each developer in previous month try to predict number of commits by each developer in the next month
 
 - Try to use PyTorch Geometric (library for graphs) or just make own simple network: 
 update vertex embeddings depending on vertex properties, embeddings of its neighbors , weights of its edges and model parameters. 
 After several iterations fix embeddings and improve other layers for several iterations
 
 - Change month period to other or tune other parameters
 
 
 
 

 
 

 