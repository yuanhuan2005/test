package com.test.hash;

public class MyMap<K, V>
{
	private int size;// ��ǰ����
	private static int INIT_CAPACITY = 16;// Ĭ������
	private Entry<K, V>[] container;// ʵ�ʴ洢���ݵ��������
	private static float LOAD_FACTOR = 0.75f;// װ������
	private int max;// �ܴ��������=capacity*factor

	// �Լ�����������װ�����ӵĹ�����
	@SuppressWarnings("unchecked")
	public MyMap(int init_Capaticy, float load_factor)
	{
		if (init_Capaticy < 0)
		{
			throw new IllegalArgumentException("Illegal initial capacity: " + init_Capaticy);
		}
		if (load_factor <= 0 || Float.isNaN(load_factor))
		{
			throw new IllegalArgumentException("Illegal load factor: " + load_factor);
		}
		MyMap.LOAD_FACTOR = load_factor;
		max = (int) (init_Capaticy * load_factor);
		container = new Entry[init_Capaticy];
	}

	// ʹ��Ĭ�ϲ����Ĺ�����
	public MyMap()
	{
		this(MyMap.INIT_CAPACITY, MyMap.LOAD_FACTOR);
	}

	/**
	 * ��
	 * 
	 * @param k
	 * @param v
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean put(K k, V v)
	{
		// 1.����K��hashֵ
		// ��Ϊ�Լ�����д���Բ�ͬ�����Ͷ����õ�Hash�㷨���ʵ���JDK������hashCode()����������hashֵ
		int hash = k.hashCode();
		//��������Ϣ��װΪһ��Entry
		Entry<K, V> temp = new Entry(k, v, hash);
		if (setEntry(temp, container))
		{
			// ��С��һ
			size++;
			return true;
		}
		return false;
	}

	/**
	 * ���ݵķ���
	 * 
	 * @param newSize
	 *            �µ�������С
	 */
	@SuppressWarnings("unchecked")
	private void reSize(int newSize)
	{
		// 1.����������
		Entry<K, V>[] newTable = new Entry[newSize];
		max = (int) (newSize * MyMap.LOAD_FACTOR);
		// 2.��������Ԫ��,����������Ԫ�أ�ÿ��Ԫ���ٴ�һ��
		for (int j = 0; j < container.length; j++)
		{
			Entry<K, V> entry = container[j];
			//��Ϊÿ������Ԫ����ʵΪ�������ԡ�������
			while (null != entry)
			{
				setEntry(entry, newTable);
				entry = entry.next;
			}
		}
		// 3.�ı�ָ��
		container = newTable;

	}

	/**
	 *��ָ���Ľ��temp��ӵ�ָ����hash��table����
	 * ���ʱ�жϸý���Ƿ��Ѿ�����
	 * ����Ѿ����ڣ�����false
	 * ��ӳɹ�����true
	 * @param temp
	 * @param table
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean setEntry(Entry<K, V> temp, Entry[] table)
	{
		// ����hashֵ�ҵ��±�
		int index = indexFor(temp.hash, table.length);
		//�����±��ҵ���ӦԪ��
		Entry<K, V> entry = table[index];
		// 3.������
		if (null != entry)
		{
			// 3.1�������������ж��Ƿ����
			while (null != entry)
			{
				//�ж���ȵ�����ʱӦ��ע�⣬���˱Ƚϵ�ַ��ͬ�⣬���ô��ݵ������equals()�����Ƚ�
				//����򲻴棬����false
				if ((temp.key == entry.key || temp.key.equals(entry.key)) && temp.hash == entry.hash
				        && (temp.value == entry.value || temp.value.equals(entry.value)))
				{
					return false;
				}
				//�������Ƚ���һ��Ԫ��
				else if (temp.key != entry.key && temp.value != entry.value)
				{
					//�����β���ж�ѭ��
					if (null == entry.next)
					{
						break;
					}
					// û�е����β������������һ��Ԫ��
					entry = entry.next;
				}
			}
			// 3.2���������˶�β�������û����ͬ��Ԫ�أ��򽫸�Ԫ�ع��ڶ�β
			addEntry2Last(entry, temp);

		}
		// 4.��������,ֱ�����ó�ʼ��Ԫ��
		setFirstEntry(temp, index, table);
		return true;
	}

	private void addEntry2Last(Entry<K, V> entry, Entry<K, V> temp)
	{
		if (size > max)
		{
			reSize(container.length * 4);
		}
		entry.next = temp;

	}

	/**
	 * ��ָ�����temp����ӵ�ָ����hash��table��ָ���±�index��
	 * @param temp
	 * @param index
	 * @param table
	 */
	@SuppressWarnings("rawtypes")
	private void setFirstEntry(Entry<K, V> temp, int index, Entry[] table)
	{
		// 1.�жϵ�ǰ�����Ƿ񳬱꣬������꣬�������ݷ���
		if (size > max)
		{
			reSize(table.length * 4);
		}
		// 2.�����꣬���������Ժ�����Ԫ��
		table[index] = temp;
		//������������������������������
		//��Ϊÿ�����ú����µ�������Ҫ�����ӵĽ�㶼ȥ��
		//NND������һ�д��뿨�˸��7��Сʱ�������ع���
		temp.next = null;
	}

	/**
	 * ȡ
	 * 
	 * @param k
	 * @return
	 */
	public V get(K k)
	{
		Entry<K, V> entry = null;
		// 1.����K��hashֵ
		int hash = k.hashCode();
		// 2.����hashֵ�ҵ��±�
		int index = indexFor(hash, container.length);
		// 3������index�ҵ�����
		entry = container[index];
		// 3��������Ϊ�գ�����null
		if (null == entry)
		{
			return null;
		}
		// 4������Ϊ�գ����������Ƚ�k�Ƿ����,���k��ȣ��򷵻ظ�value
		while (null != entry)
		{
			if (k == entry.key || entry.key.equals(k))
			{
				return entry.value;
			}
			entry = entry.next;
		}
		// ����������˲���ȣ��򷵻ؿ�
		return null;

	}

	/**
	 * ����hash�룬��������ĳ���,����ù�ϣ�������������е��±�ֵ
	 * 
	 * @param hashcode
	 * @param containerLength
	 * @return
	 */
	public int indexFor(int hashcode, int containerLength)
	{
		return hashcode & (containerLength - 1);

	}

	/**
	 * ����ʵ�ʱ������ݵ��ڲ���,��Ϊ���ù����������ͻ�����ڲ������Ϊ������ʽ
	 * 
	 * @param <K>key
	 * @param <V>
	 *            value
	 */
	@SuppressWarnings("hiding")
	class Entry<K, V>
	{
		Entry<K, V> next;// ��һ�����
		K key;// key
		V value;// value
		int hash;// ���key��Ӧ��hash�룬��Ϊһ����Ա���������´���Ҫ�õ�ʱ����Բ������¼���

		// ���췽��
		Entry(K k, V v, int hash)
		{
			this.key = k;
			this.value = v;
			this.hash = hash;

		}

		//��Ӧ��getter()����

	}
}
