# simpleSim
A *very* simple simulation I wrote in Java for fun one day.

# The Idea
Well, to be honest, there isn't much to it. I was just wondering one day how much the strength of a given entity would affect it's ability and consistency of winning (in a battle-royale style environment. That wasn't my intention per se, but that's more or less what the result is), so I wrote a very basic gui, entity object, and ai for a given amount of entities, gave them all random attributes (including health, damage, and a small likelihood to be pacifist and not damage others) and set them all into a small field to face off against each other.

# The Attributes
The attributes I have so far for each entity (including unset or subject-to-change ones) are: health, damage, speed (currently static due to how I implemented movement), friends - individuals the entity won't attack, and name (random, kind of useless at the moment).

# Pacifists?
Pacifists are a 1/10 chance when an entity is created, and they only "shoot" bullets that heal their targets. They also consider everyone their friend. This, in conjuction with the fact that entities have a chance to befriend someone who heals them, can cause a somewhat unintended phenomenon where two players team up and assist each other in the environment. I tried to make the pacifist entities pure white for clarity, but I don't think that always works, unfortunately.