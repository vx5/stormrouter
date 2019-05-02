function reverseArrow(position) {
  switch (position) {
    case "left":
      return "right";
    case "right":
      return "left";
    case "up":
      return "down";
    case "down":
      return "up";
    default:
      return null;
  }
}

function collapse(element, buttonElem) {
  if (element.style.display === 'none') {
    element.style.display = "";
  } else {
    element.style.display = "none";
  }

  const button = buttonElem.classList[1].split('-');
  const newButton = 'fa-angle-' + reverseArrow(button[2]);
  buttonElem.className = "fas " + newButton;
}

function showContent(element, buttonElem){
  if (element.style.display === 'none')
    collapse(element, buttonElem);
}

const $collapse = $(".collapse");
const $collapsable = $(".collapsable");
const $arrow = $(".collapse > i");

$(document).ready(() => {  
  for (let i = 0; i < $collapse.length; i++) {
    const cur = $($collapse[i]);
    cur.click(() => {
      collapse($collapsable[i], $($arrow[i])[0]);
    })
  }
});