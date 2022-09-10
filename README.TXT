Description:
Simulation runs by randomly spawning different numbers of the five organisms at random locations on the map. Then implements the already coded behaviors and interactions between the organisms and the environment, following the food chain which dictates what organism can consume the other.


Types of Species & Behaviors:
•	Grass:
	Consumed by Possums and Zebra
	Can reproduce
	Immobile
	Can die of old age
•	Possum:
	Consumes Grass
	Is able to reproduce (with the opposite gender)
	Can be infected with a disease
	Can Die from hunger, overcrowding, and age
	Can move to different locations across the terrain
•	Zebra:
	Consumes Grass
	Is able to reproduce (with the opposite gender)
	Can be infected with a disease
	Can Die from hunger, overcrowding, and age
	Can move to different locations across the terrain
•	Wolf:
	Is able to consume Possums and Zebras
	Is able to reproduce (with the opposite gender)
	Can be infected with a disease
	Can Die from hunger, overcrowding, and age
	Can move to different locations across the terrain
•	Lion:
	Is able to consume Possums and Zebras 
	Is able to reproduce (with the opposite gender)
	Can be infected with a disease
	Can Die from hunger, overcrowding, and age
	Can move to different locations across the terrain


1) Grass is made a subclass of the Species class, its only actions are to reproduce, grass dies when it is eaten or if it gets too old.
2) All animals are aware of the time and weather but only Possums and Zebras are affected by changes, their feeding period is dependent on the weather and time of day.

3) All plants and animals have a probability of being born sick and this disease can also be spread through mating with a sick animal.

Implemented a graph view of the simulation to represent the increase and decrease of each type of species in the environment. The Simulator View class is turned to an interface which is implemented by two classes that responsible for the graph and grid. 

