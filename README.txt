Group 0220 Restaurant Program, Phase 1
John, Luke, Shien, and Victor


New: To be moved into README: Format for Table.txt: table, tableCapacity | tableAmount
     Note - Precondition: total tableAmount must equal 20.

     To be moved into README: Format for Staff.txt: staffType | staffName1, staffName2, ..., staffNameN

----OVERVIEW----

Our restaurant program is written to have 3 servers, 2 chefs, and a manager, and a 20-cell array of Bills to represent 20 tables.
To add more or remove staff members, one must initialize more and add them manually in the Restaurant class.
There is no events.txt line to support this.

-> THE MENU:

The menu of our restaurant is stored in menu.txt, which MenuItem reads to create an indexed collection of orderable items.
The format of menu is:

    [menuIndex], [dishName], [dishPrice] | [ingredient1],[ingredient1 qty] | [ingredient2],[ingredient2 qty] | ... | [ingredientN] [ingredientN qty]

Note: each ingredient must appear at most once and must exist in the restaurant's inventory.

-> THE INVENTORY:

The restaurant's entire stock of ingredients is written in ingredients.txt and read into the Inventory class.
The format of the inventory is:

    [ingredient],[quantity],[threshold]

[ingredient] must be the same as its name in menu.txt. For consistency, all ingredient strings must be singular and first-letter-capitalized.
[threshold] is the quantity at which the inventory notifies the manager for a restock.

Our program contains no method to "reset" the inventory to a default value.
To revert the inventory to its state during our final push, clear ingredients.txt and copy-paste the contents of ingredientReset.txt into it.

-> EVENTS:

The events.txt file is read by the EventReader class, which houses our program's main method.
Note: adjacent items in every line of events.txt has a single space between them.



----ORDERING----

The default event to represent a server entering a new Order into the system is:

    Order [menuIndex] [tableNumber]

where [menuIndex] is the dish's entry number in the restaurant's menu, and [tableNumber] indicates the table that ordered it.

To specify additions and subtractions of the ingredients in the dish, an extension is required:

    Order [menuIndex] [tableNumber] [ingredient1] [ingredient1 qty] ... [ingredientN] [ingredientN qty]

where the qty fields indicate the number by which the ingredient quantity is changed. A negative input results in a removal of ingredients.

Every order is given an order number at its moment of creation.



----ADVANCING AN ORDER----

After an order is entered into the system, to advance its progress, the following general format is used:

    [newOrderState] Order [orderNumber]

[newOrderState] can be the following words (entered without quotations):
    "Seen": The next unoccupied chef confirms that he has seen this order and starts preparing it
    "Prepared": This order is confirmed to be ready, and the next available server picks it up
    "Delivered": This order is confirmed to be delivered to the table and added to the table's bill
    "Cancel": Something about this order has gone wrong after delivery, and it's removed from the bill
    "Reorder": Something about this order has gone wrong after delivery, and it's cancelled and entered back into the system

These order advancements must be made in the following sequence: Seen -> Prepared -> Delivered -> Cancel/Reorder.



----MANAGER----

To represent the manager checking the inventory:

    Manager check

This will print the entire inventory to the screen.

To represent the manager ordering a restock delivery for [ingredient] by the default amount (20):

    Manager order [ingredient]

To represent the manager modifying restocking request amounts:

    Manager order [ingredient] [targetQuantity]

This will change the restock request amount of [ingredient] to [targetQuantity].



----BILL----

The event representing the printing of a bill for a table and displaying it on the screen:

    Bill print [tableNum]

To represent paying a bill, type:

    Bill pay [tableNum]