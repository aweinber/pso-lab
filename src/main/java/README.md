PSO Neighborhood Topologies

-----------

Main.java is the file which reads all of the command line arguments, and makes sure they are correct. It then initiallizes all of the particles, the swarm, and the neighborhoods. After that it runs each iteration of the pso in a for-loop.

Swarm.java represents the swarm class. It holds the array of particles in the swarm, a dictionary matching each particle to its neighborhood, a variable for the problems topology, a variable for the best global point found, and a variable for the location of that best global point. Its initializer takes an array of particles and a topology, to initialize the swarm. Its initializeNeighborhood method initializes neighborhoods. Its move function runs one interation of pso and updates all of the necessary best locations found.

The Neighborhood class represents a neighborhood for a specific point. It has an array of particles representing that points neighbors, and a best location found with a value and a location. Its function updateNBest updates the neighborhood best.

The Particle class represents a particle with a location. This class can evaluate that location based on the function of the problem.

