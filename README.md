# Filter Iterator

This package provides a framework for users to generate iterators that will filter values based on a iterator. For example,
if an ArrayList contains the values [1, 2, 5, 7], then a FilterIterator of that ArrayList predicated on the values of next()
being even will only output one value - 2.

Single predicates can be defined, as well as any complex conjunction of predicates based on the logical and/or/not boolean
operators. 
