#импорты

import asyncio
import logging
from asyncio import timeout
import httpx
from aiogram import Bot, Dispatcher, F, Router
from aiogram.types import Message, InlineKeyboardMarkup, InlineKeyboardButton, CallbackQuery
from aiogram.filters import CommandStart
import aiohttp

#создание диспетчера
BOT_TOKEN = "8942039805:AAF1ellog3ABmhhmQuvQ7mXWCSGVhKV76iE"
bot = Bot(token=BOT_TOKEN)
BACKEND_URL = "https://nailsbot-90fb.onrender.com/api/v1"
dp = Dispatcher()

#обработка команды старт

urlbutton = InlineKeyboardButton(text="💅 Записаться на ноготочки", callback_data="запись")
urlbutton2 = InlineKeyboardButton(text="💎 Услуги & Прайс", callback_data="услуги")
urlbutton3 = InlineKeyboardButton(text="📸 Галерея работ", callback_data="галерея")
urlbutton4 = InlineKeyboardButton(text="📍 Локация ", callback_data="адрес")
urlbutton5 = InlineKeyboardButton(text="💌 Написать Насте", callback_data="контакты")
keyboard = InlineKeyboardMarkup(
    inline_keyboard=[
           [urlbutton],
           [urlbutton2,urlbutton3],
           [urlbutton4, urlbutton5]

    ]
)

@dp.message(CommandStart())
async def send_welcome(message: Message):
    await message.answer(f"Привет {message.from_user.first_name}! 👋\n"
                         "я бот для записи на маникюр. Выберите действие:",reply_markup=keyboard )


#CallbackQuery
@dp.callback_query()
async def process(callback: CallbackQuery):
    await callback.answer()
    if callback.data == "запись":
        await  callback.message.answer("запись")
    elif callback.data == "галерея":
        await  callback.message.answer("галерея")
    elif callback.data == "адрес":
        await  callback.message.answer("адрес")
    elif callback.data == "контакты":
        await  callback.message.answer("контакты")
    elif callback.data == "услуги":
        await callback.message.answer("услуги")

#запуск бота
async def main():
    await dp.start_polling(bot)

if __name__ == '__main__':
    asyncio.run(main())
