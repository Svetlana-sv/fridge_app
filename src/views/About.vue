<template>
  <div class="web-app">
    <div class="header">
      <h2>MyFridge, page: {{page+1}}</h2>
      <div class="nav">
        <img v-if="page===0" src="../assets/cook_book.png" @click="page=2" alt="toCookBook" width="40">
        <img v-else-if="page===3" src="../assets/back.png" @click="page=2" alt="toCookBook" width="40">
        <img v-else src="../assets/back.png" @click="page=0" alt="toCookBook" width="40">
      </div>
    </div>
    <!-- ---------------------------------FRIDGES-------------------------------------- -->
    <div v-if="page===0" class="main">
      <div class="main__flex-container">
        <div @click="toProductsPage(fridge.food)" class="fridge" v-for="(fridge,index) in fridges" :key="index">
          <h3>{{fridge.title}}</h3>
          <img src="../assets/fridge_pic.png" class="img" alt="fridge">
        </div>
      </div>
      <button @click="ShowModal(page)" >Добавить холодильник</button>
    </div>
    <!-- --------------------------------PRODUCTS--------------------------------------- -->
    <div v-else-if="page===1" class="main">
      <div class="search-container">
        <input type="text" class="search-input" placeholder="Поиск по продуктам" v-model="search">
      </div>
      <div class="main__flex-container">
        <div class="product">
          <h3>Название</h3>
          <h3>Срок годности</h3>
        </div>
        <div class="product" v-for="(product,index) in filtred_products" :key="index">
          <h3>{{product.title}}</h3>
          <h3>{{product.date}}</h3>
        </div>
      </div>
      <button @click="ShowModal(page)" >Добавить продукт</button>
    </div>
     <!-- --------------------------------COOK-BOOK--------------------------------------- -->
    <div v-else-if="page===2" class="main">
       <div class="search-container">
        <input type="text" class="search-input" placeholder="Поиск по рецептам" v-model="search">
      </div>
      <div class="main__flex-container">
        <div class="reciept">
          <h3 style="text-decoration:underline;">Название рецепта</h3>
        </div>
        <div @click="toRecieptPage(index,reciept.descroption)" class="reciept pointer" v-for="(reciept,index) in filtred_reciepts" :key="index">
          <h3>{{reciept.title}}</h3>
        </div>
      </div>
      <button @click="ShowModal(page)" >Добавить рецепт</button>
    </div>
     <!-- --------------------------------COOK-BOOK-RECIEPT-------------------------------------- -->
      <div v-else class="main">
        <h2 style="text-decoration:underline;">{{reciepts[c_index_res].title}}</h2>
        <textarea class="texterea" v-model="c_description" name="Text1" cols="40" rows="5">
        </textarea>
        <button @click="SaveReciept()" >Сохранить</button>
      </div>
    <!-- ---------------------------------MODAL------------------------------------- -->
    <div class="modal" id="modal">
      <div class="modal__content">
        <p>{{modalTitle}}</p>
        <input v-model="inputText" id="modal-input" type="text">
        <div>
          <button @click="Commit(page)">Подтвердить</button>
          <button @click="Cancel(page)">Отмена</button>
        </div>
      </div>
    </div>
     <!-- ----------------------------------MODAL-1----------------------------------------- -->
    <div class="modal" id="modal-1">
      <div class="modal__content">
        <p>{{modalTitle}}</p>
        <input v-model="inputText" id="modal-input-1" type="text">
        <p>Введите дату окончания срока годности</p>
        <input type="date" id="modal-input-1-date">
        <div>
          <button @click="Commit(page)">Подтвердить</button>
          <button @click="Cancel(page)">Отмена</button>
        </div>
      </div>
    </div>
    <!-- ----------------------------------MODAL-2----------------------------------------- -->
     <div class="modal" id="modal-2">
      <div class="modal__content">
        <p>Введите название рецепта</p>
        <input v-model="inputText" id="modal-input-2" type="text">
        <div>
          <button @click="Commit(page)">Подтвердить</button>
          <button @click="Cancel(page)">Отмена</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      fr_count: 0,
      fridges: [],
      products: [],
      reciepts: [],
      page: 0,
      c_index_res: -1,
      search: '',
      c_description: '',
      modalTitle: 'Введите название',
      inputText: ''
    }
  },
  methods: {
    SaveReciept () {
      this.reciepts[this.c_index_res].descroption = this.c_description
    },
    ShowModal (Page) {
      this.inputText = ''
      if (Page === 0) {
        var modal = document.getElementById('modal')
        document.getElementById('modal-input').style.border = '1px solid black'
        modal.style.display = 'block'
      } else if (Page === 1) {
        document.getElementById('modal-input-1').style.border = '1px solid black'
        document.getElementById('modal-input-1-date').style.border = '1px solid black'
        var modal1 = document.getElementById('modal-1')
        modal1.style.display = 'block'
      } else {
        document.getElementById('modal-input-2').style.border = '1px solid black'
        var modal2 = document.getElementById('modal-2')
        modal2.style.display = 'block'
      }
    },
    Commit (Page) {
      if (Page === 0) {
        var input = document.getElementById('modal-input')
        if (input.value !== '') {
          var modal = document.getElementById('modal')
          var fridge = { title: this.inputText, food: [] }
          this.fridges.push(fridge)
          this.fr_count++
          modal.style.display = 'none'
        } else {
          input.style.border = '2px solid red'
        }
      } else if (Page === 1) {
        var inp1 = document.getElementById('modal-input-1')
        var inp2 = document.getElementById('modal-input-1-date')
        if (inp1.value !== '' && inp2.value !== '') {
          this.products.push({ title: inp1.value, date: inp2.value })
          var modal1 = document.getElementById('modal-1')
          modal1.style.display = 'none'
        } else {
          inp1.style.border = '2px solid red'
          inp2.style.border = '2px solid red'
        }
      } else {
        var input2 = document.getElementById('modal-input-2')
        if (input2.value !== '') {
          var modal2 = document.getElementById('modal-2')
          var reciept = { title: input2.value, descroption: '' }
          this.reciepts.push(reciept)
          modal2.style.display = 'none'
        } else {
          input2.style.border = '2px solid red'
        }
      }
    },
    toProductsPage (food) {
      //  console.log(food)
      this.page = 1
      this.products = food
    },
    toRecieptPage (index, description) {
      this.page = 3
      this.c_index_res = index
      this.c_description = description
    },
    Cancel (Page) {
      var modal = document.getElementById('modal')
      modal.style.display = 'none'
      var modal1 = document.getElementById('modal-1')
      modal1.style.display = 'none'
      var modal2 = document.getElementById('modal-2')
      modal2.style.display = 'none'
    }
  },
  computed: {
    filtred_products: function () {
      var keyword = this.search
      return this.products.filter(
        (x) =>
          x.title.toLowerCase().includes(keyword))
    },
    filtred_reciepts: function () {
      var keyword = this.search
      return this.reciepts.filter(
        (x) =>
          x.title.toLowerCase().includes(keyword))
    }
  }
}
</script>

<style scoped>
.web-app{
  /* border: 1px solid black; */
  padding: 0px 20%;
  margin-bottom: 200px;
}
.header{
  background-color: rgb(0, 236, 222);
  border-radius:20px 20px 0px  0px ;
  padding: 5px 20px ;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
img:hover{
  cursor: pointer;
}
.main{
  /* height: 800px; */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  padding-bottom: 20px;
  border-left: 1px solid black;
  border-right: 1px solid black;
  border-bottom: 1px solid black;
  border-radius: 0 0 10px 10px;
}
.main button{
  border: 1px solid black;
  background-color: white;
  border-radius: 10px;
  width: 200px;
  padding: 10px 20px;
}
.main__flex-container{
  padding: 10px 0px ;
  display: flex;
  width: 80%;
  gap: 10px;
  flex-direction: column;
  min-height: 500px;
}
.modal{
  display: none;
  position: fixed;
  top: 40%;
  left: 30%;
  right: 30%;
  background-color: white;
  border: 1px solid black;
  padding: 20px 0px;
  border-radius: 10px;
}
input{
  padding: 5px;
  outline: none;
  width: 70%;
}
.modal__content{
  display: flex;
  flex-direction: column;
  gap: 10px;
  justify-content: center;
  align-items: center;
}
.modal__content button{
  padding: 10px;
  margin: 10px;
}
.fridge{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  border-radius: 20px;
  border: 1px solid black;
}
.img{
  width: 80%;
}
.product{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  border-radius: 10px;
  border: 1px solid rgb(0, 236, 222);
  padding: 10px 30px;
}
.product:hover{
  color: white;
  background-color: rgb(0, 236, 222);
}
.fridge:hover{
  color: white;
  background-color: rgb(0, 236, 222);
}
 button:hover{
   color: white;
   border:1px solid rgb(0, 236, 222);
   background: rgb(0, 236, 222);
   cursor: pointer;
 }
 .search-input{
   padding: 10px;
   border-radius: 10px;
   border: 1px solid black;
 }
 .search-container{
   display: flex;
   width: 100%;
   justify-content: center;
   border-bottom: 1px solid black;
   padding:10px 0px;
 }
 .reciept{
  display: flex;
  flex-direction: row;
  justify-content: center;
  border-radius: 10px;
  border: 1px solid rgb(0, 236, 222);
  padding: 10px 30px;
 }
.pointer:hover{
  color: white;
  background-color: rgb(0, 236, 222);
}
.pointer{
  cursor: pointer;
}
.texterea{
  margin-top: 20px;
  min-height: 500px;
  border: 1px solid rgb(0, 236, 222);
  max-width: 80%;
}
</style>
