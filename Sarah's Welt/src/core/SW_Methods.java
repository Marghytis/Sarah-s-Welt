public void expand(Node conn, float dest){
	//define direction
	boolean right = dest > conn.p.x;

	//create the line which starts at the connection
	Node n1 = conn;
	while(right ? n1.p.x < dest : n1.p.x > dest){
		n1.next = DB.getNode(n.nextIndex));
		n1.next.last = n1;
		n1 = n1.getNext();
	}

	//create the second line, which starts at the other part of the connection
	Node n2 = conn.getNext();
	while(right ? n2.p.x < dest : n2.p.x > dest){
		n2.last = DB.getNode(n.lastIndex);
		n2.last.next = n2;
		n2 = n2.getLast();
	}

	//connect both lines
	n.next = n2;//not set(), because the index should stay, the index is global and can refer to Nodes that aren't loaded
	n2.last = n;
}

public void contract(Node end, float dest){
	//define direction
	boolean right = dest > conn.p.x;

	//save the line which goes back from the end
	Node n1 = end;
	while(right ? n1.p.x < dest : n1.p.x > dest){
		DB.saveNode(n1);
		n1 = n1.getLast();
	}

	//save the other line, which continues after the end to go back
	Node n2 = end.getNext();
	while(right ? n2.p.x < dest : n2.p.x > dest){
		DB.saveNode(n2);
		n1 = n1.getNext();
	}

	//connect the two shortened lines again (not their indices - they have to stay for the database)
	n1.next = n2;//not set(), because the index should stay, the index is global and can refer to Nodes that aren't loaded
	n2.last = n1;
}