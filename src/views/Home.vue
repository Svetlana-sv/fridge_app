<template>
  <div class="home">
    <div class="home__flex-container">
      <div class="flex-container__text-container">
        <h2>«Учет продуктов»</h2>
        <p class="text-container__title">Проект предназначен для учета продуктов в вашем холодильнике
        </p>
        <p>Основной частью проекта является мобильное приложение, которое позволяет пользователю без подключения к Интернет сети
          создавать и вести виртуальный холодильник, добавлять в него определенную еду,
          указывая при этом указывая срок её годности и количество,
          таким образом контролируя наличие или отсутствие нужных продуктов,
          также предусмотрена настройка автоматической проверки холодильника на испорченность продуктов,
          за выбранный период времяни</p>
      </div>
      <img class="flex-container__foto-container" src="..\assets\scrn1.jpg" alt="scrn1"/>
    </div>
    <div class="home__flex-container column">
        <h1>Другие скриншоты из приложения</h1>
        <div class="home_grid-container">
          <img class="flex-container__foto-container" src="..\assets\scrn1.jpg" alt="scrn1"/>
          <img class="flex-container__foto-container" src="..\assets\scrn2.jpg" alt="scrn2"/>
          <img class="flex-container__foto-container" src="..\assets\scrn3.jpg" alt="scrn3"/>
          <img class="flex-container__foto-container" src="..\assets\scrn4.jpg" alt="scrn4"/>
          <img class="flex-container__foto-container" src="..\assets\scrn5.jpg" alt="scrn5"/>
          <img class="flex-container__foto-container" src="..\assets\scrn6.jpg" alt="scrn6"/>
          <img class="flex-container__foto-container" src="..\assets\scrn7.jpg" alt="scrn7"/>
        </div>
    </div>
    <div class="home__flex-container column" style="margin-bottom: 50px">
        <h1>Участники проекта (191-361)</h1>
        <div>Светлана Каменских</div>
        <div>Егор Алексеев</div>
        <div>Кирилл Афанасьев</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  components: {}
}
</script>

<style scoped>
.home__flex-container{
  display: flex;
  justify-content: center;
  flex-direction: row;
  gap: 40px;
  padding: 35px 20%;
  align-items: flex-start;
  border-bottom: 1px solid rgb(0, 236, 222);
}
.flex-container__text-container{
  max-width: 500px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: justify;
}
.flex-container__foto-container{
  width: 300px;
  border: 1px solid black;
  border-radius: 20px;
  margin: 20px;
  box-shadow: 1px 4px 4px 2px rgba(0, 0, 0, 0.25);
}
.text-container__title{
  font-weight: bold;
}

.column{
  align-items: center;
  flex-direction: column;
  padding: 35px 0px;
}
.grid-container__item{
    display: inline;
}
.column div{
  margin: 0;
  padding: 5px;
  font-weight: bold;
}
@media (max-width: 1000px) {
  .flex-container__foto-container{
    width: 200px;
  }
}
@media (max-width: 900px) {
  .home__flex-container{
    padding: 20px 10%;
    flex-direction: column;
    align-items: center;
  }
}
</style>
