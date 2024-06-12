
let formRestorant = function (){
    //récupération et affichage du template stationInfo
    let htmlFormResto = document.getElementById("creationRestoTemplate");
    let formRestoTemplate = document.getElementById("formResto");
    let template = Handlebars.compile(formRestoTemplate.innerHTML);
    htmlFormResto.innerHTML = template({});
}

export default {
    formRestorant: formRestorant,
}

/*<div contextmenu="menu" id="box">
	Right click to get the context menu demo.
</div>

<menu type="context" id="menu">

	<menuitem type="checkbox" id="move">Moooove</menuitem>

	<menuitem type="command" label="Test me" id="custom">Hello World!</menuitem>

	<menuitem type="radio" name="aligngroup" value="left">Align Left</menuitem>

	<menuitem type="radio" name="aligngroup" value="right">Align Right</menuitem>

	<menuitem type="radio" name="aligngroup" checked="true" value="center">Align Center</menuitem>

	<menuitem type="checkbox" disabled>Disabled menu item</menuitem>
</menu>*/