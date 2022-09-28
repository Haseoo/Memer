import redis

if __name__ == '__main__':
    client = redis.Redis(host='localhost', port=6379)

    for key in client.scan_iter('*'):
        server_id = key.decode().split('#')[0]
        meme = client.hgetall(key)
        new_key = 'meme:' + server_id + ':' + (meme[b'name'].decode()).lower()
        client.hmset(new_key, meme)
        print('Imported: ', new_key)